package dev.forcetower.instrack.core.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener
import com.android.billingclient.api.acknowledgePurchase
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcetower.instrack.core.model.billing.AugmentedSkuDetails
import dev.forcetower.instrack.core.model.billing.PremiumStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.HashSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: BillingDB
) : PurchasesUpdatedListener, BillingClientStateListener, SkuDetailsResponseListener {
    private lateinit var billingClient: BillingClient

    val premiumStatus: LiveData<PremiumStatus> by lazy {
        database.entitlements().getPremiumStatus().map {
            it ?: PremiumStatus(false)
        }.asLiveData()
    }

    val subscriptions: LiveData<List<AugmentedSkuDetails>> by lazy {
        database.skuDetails().getSubscriptionSkuDetails().asLiveData()
    }

    fun initializeConnections() {
        Timber.d("Initializing connections")
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        connectToGooglePlay()
    }

    fun endConnection() {
        billingClient.endConnection()
    }

    fun launchBillingFlow(activity: Activity, details: AugmentedSkuDetails) {
        val sku = SkuDetails(details.originalJson)
        val params = BillingFlowParams.newBuilder()
            .setSkuDetails(sku)
            .build()

        billingClient.launchBillingFlow(activity, params)
    }

    private fun connectToGooglePlay(): Boolean {
        Timber.d("Connecting to google play")
        if (!billingClient.isReady) {
            billingClient.startConnection(this)
            return true
        }
        return false
    }

    private fun processPurchases(purchases: List<Purchase>) = CoroutineScope(Job() + Dispatchers.IO).launch {
        Timber.d("Process purchases: $purchases")
        val cachedPurchases = database.purchases().getPurchases()
        val newBatch = HashSet<Purchase>(purchases.size)
        purchases.forEach { purchase ->
            if (isSignatureValid(purchase) && !cachedPurchases.any { it.data == purchase }) {
                newBatch.add(purchase)
            }
        }

        if (newBatch.isNotEmpty()) {
            saveToLocalDatabase(newBatch)
        }

        if (purchases.none { SUBS_SKUS.contains(it.sku) }) {
            val premium = PremiumStatus(false)
            database.entitlements().insert(premium)
            SUBS_SKUS.forEach { sku ->
                database.skuDetails().insertOrUpdate(sku, true)
            }
        }
    }

    private suspend fun saveToLocalDatabase(newBatch: Set<Purchase>) {
        withContext(Dispatchers.IO) {
            newBatch.forEach { purchase ->
                when (purchase.sku) {
                    WEEK_SUB, MONTH_SUB -> {
                        val premium = PremiumStatus(true)
                        database.entitlements().insert(premium)
                        database.skuDetails().insertOrUpdate(purchase.sku, false)
                        SUBS_SKUS.forEach { otherSku ->
                            if (otherSku != purchase.sku) {
                                database.skuDetails().insertOrUpdate(otherSku, true)
                            }
                        }
                        if (!purchase.isAcknowledged) {
                            val params = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()
                            val response = billingClient.acknowledgePurchase(params)
                            if (response.responseCode != BillingClient.BillingResponseCode.OK) {
                                Timber.e("Purchase was not acknowledge with code: ${response.responseCode} >> ${response.debugMessage}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isSignatureValid(purchase: Purchase): Boolean {
        return Security.verifyPurchase(Security.BASE_64_ENCODED_PUBLIC_KEY, purchase.originalJson, purchase.signature)
    }

    fun queryPurchases() {
        if (isSubscriptionSupported()) {
            val result = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                result.purchasesList?.let { processPurchases(it) }
            } else {
                Timber.i("Query purchases failed ${result.billingResult.debugMessage}")
            }
        } else {
            Timber.i("Subscription is not supported...")
        }
    }

    private fun querySkuDetails(@BillingClient.SkuType skuType: String, skuList: List<String>) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(skuType)
        billingClient.querySkuDetailsAsync(params.build(), this)
    }

    private fun isSubscriptionSupported(): Boolean {
        val response = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (response.responseCode != BillingClient.BillingResponseCode.OK) {
            Timber.w("isSubscriptionSupported() got an error response: ${response.responseCode} ${response.debugMessage}")
        }
        return response.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onSkuDetailsResponse(result: BillingResult, details: MutableList<SkuDetails>?) {
        Timber.d("Sku details response $details")
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            Timber.i("SkuDetails query failed with response: ${result.responseCode}")
        } else {
            val scope = CoroutineScope(Job() + Dispatchers.IO)
            scope.launch {
                details?.forEach {
                    database.skuDetails().insertOrUpdate(it)
                }
            }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> purchases?.let { processPurchases(it) }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Timber.i("Item already owned... querying purchases to update")
                queryPurchases()
            }
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> Timber.i("A developer error... ${result.debugMessage}")
            else -> Timber.i("BillingClient.BillingResponse onPurchasesUpdated error code: ${result.responseCode} >> ${result.debugMessage}")
        }
    }

    override fun onBillingSetupFinished(result: BillingResult) {
        Timber.d("Billing setup finished ${result.responseCode}")
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                querySkuDetails(BillingClient.SkuType.SUBS, SUBS_SKUS)
                Timber.d("Query sku details... ${BillingClient.SkuType.SUBS}")
                queryPurchases()
            }
            else -> Timber.i("BillingClient.BillingResponse onBillingSetupFinished error code: ${result.responseCode} >> ${result.debugMessage}")
        }
    }

    override fun onBillingServiceDisconnected() {
        Timber.i("Disconnected to PlayBilling")
    }

    companion object {
        const val WEEK_SUB = "instracker_common_weekly"
        const val MONTH_SUB = "instracker_common_monthly"
        val SUBS_SKUS = listOf(MONTH_SUB)
    }
}
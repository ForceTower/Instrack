package dev.forcetower.instrack.view

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.instrack.core.billing.BillingRepository
import dev.forcetower.instrack.core.model.billing.AugmentedSkuDetails
import dev.forcetower.instrack.core.model.billing.PremiumStatus
import timber.log.Timber

class BillingViewModel @ViewModelInject constructor(
    private val repository: BillingRepository
) : ViewModel() {
    val premiumStatus: LiveData<PremiumStatus>
    val subscriptions: LiveData<List<AugmentedSkuDetails>>

    init {
        repository.initializeConnections()
        premiumStatus = repository.premiumStatus
        subscriptions = repository.subscriptions
    }

    fun makePurchase(activity: Activity, details: AugmentedSkuDetails) {
        repository.launchBillingFlow(activity, details)
    }

    fun queryPurchases() {
        Timber.d("Requests a query purchases")
        repository.queryPurchases()
    }

    override fun onCleared() {
        super.onCleared()
        repository.endConnection()
    }
}

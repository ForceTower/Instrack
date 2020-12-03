package dev.forcetower.instrack.core.billing

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.forcetower.instrack.core.billing.dao.AugmentedSkuDetailsDao
import dev.forcetower.instrack.core.billing.dao.EntitlementDao
import dev.forcetower.instrack.core.billing.dao.PurchaseDao
import dev.forcetower.instrack.core.model.billing.AugmentedSkuDetails
import dev.forcetower.instrack.core.model.billing.CachedPurchase
import dev.forcetower.instrack.core.model.billing.PremiumStatus

@Database(
    entities = [
        AugmentedSkuDetails::class,
        PremiumStatus::class,
        CachedPurchase::class
    ],
    version = 1
)
abstract class BillingDB : RoomDatabase() {
    abstract fun purchases(): PurchaseDao
    abstract fun entitlements(): EntitlementDao
    abstract fun skuDetails(): AugmentedSkuDetailsDao
}

package dev.forcetower.instrack.core.billing.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.android.billingclient.api.Purchase
import dev.forcetower.instrack.core.model.billing.CachedPurchase

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM CachedPurchase")
    suspend fun getPurchases(): List<CachedPurchase>

    @Insert
    suspend fun insert(purchase: CachedPurchase)

    @Transaction
    suspend fun insert(vararg purchases: Purchase) {
        purchases.forEach {
            insert(CachedPurchase(data = it))
        }
    }

    @Delete
    suspend fun delete(vararg purchases: CachedPurchase)

    @Query("DELETE FROM CachedPurchase")
    suspend fun deleteAll()
}
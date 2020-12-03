package dev.forcetower.instrack.core.billing.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.forcetower.instrack.core.model.billing.PremiumStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface EntitlementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: PremiumStatus)

    @Update
    suspend fun update(status: PremiumStatus)

    @Query("SELECT * FROM PremiumStatus LIMIT 1")
    fun getPremiumStatus(): Flow<PremiumStatus?>

    @Delete
    suspend fun delete(status: PremiumStatus)
}

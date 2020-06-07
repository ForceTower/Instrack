package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.SyncRegistry
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SyncRegistryDao : BaseDao<SyncRegistry>() {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertGettingId(value: SyncRegistry): Long

    @Query("UPDATE SyncRegistry SET account = :status, accountError = :error WHERE id = :syncId")
    abstract fun updateAccount(syncId: Long, status: Boolean, error: Boolean)

    @Query("UPDATE SyncRegistry SET followers = :status, followersError = :error WHERE id = :syncId")
    abstract fun updateFollowers(syncId: Long, status: Boolean, error: Boolean)

    @Query("UPDATE SyncRegistry SET following = :status, followingError = :error WHERE id = :syncId")
    abstract fun updateFollowing(syncId: Long, status: Boolean, error: Boolean)

    @Query("UPDATE SyncRegistry SET stories = :status, storiesError = :error WHERE id = :syncId")
    abstract fun updateStories(syncId: Long, status: Boolean, error: Boolean)

    @Query("UPDATE SyncRegistry SET feed = :status, feedError = :error WHERE id = :syncId")
    abstract fun updateFeed(syncId: Long, status: Boolean, error: Boolean)

    @Query("SELECT * FROM SyncRegistry ORDER BY createdTime DESC LIMIT 1")
    abstract fun getLatestSync(): Flow<SyncRegistry?>

    @Query("SELECT * FROM SyncRegistry ORDER BY createdTime DESC LIMIT 1")
    abstract fun getLatestSyncDirect(): SyncRegistry?
}
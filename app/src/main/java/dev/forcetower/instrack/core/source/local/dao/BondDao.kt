package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.forcetower.instrack.core.model.database.ProfileBond
import dev.forcetower.instrack.core.model.database.ProfileBondFollower
import dev.forcetower.instrack.core.model.database.ProfileBondFollowing
import dev.forcetower.instrack.core.model.database.ProfileFriendship
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class BondDao : BaseDao<ProfileBond>() {
    @Query("SELECT * FROM ProfileBond WHERE followsMe = 1 AND referencePk = :userPk")
    abstract suspend fun getFollowersSnapshot(userPk: Long): List<ProfileBond>

    @Query("SELECT * FROM ProfileFriendship WHERE userPk = :userPk")
    abstract suspend fun getFollowingSnapshot(userPk: Long): List<ProfileFriendship>

    @Query("SELECT * FROM ProfileBond WHERE userPk = :userPk AND referencePk = :referencePk")
    abstract suspend fun getItem(userPk: Long, referencePk: Long): ProfileBond?

    @Transaction
    open suspend fun insertOrUpdateFollower(list: List<ProfileBondFollower>) {
        list.forEach { element ->
            val item = getItem(element.userPk, element.referencePk)
            if (item == null) {
                insert(ProfileBond.adapt(element))
            } else {
                update(element)
            }
        }
    }

    @Transaction
    open suspend fun insertOrUpdateFollowing(list: List<ProfileBondFollowing>) {
        list.forEach { element ->
            val item = getItem(element.userPk, element.referencePk)
            if (item == null) {
                insert(ProfileBond.adapt(element))
            } else {
                update(element)
            }
        }
    }

    @Update(entity = ProfileBond::class, onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun update(bond: ProfileBondFollower)

    @Update(entity = ProfileBond::class, onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun update(bond: ProfileBondFollowing)
}
package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.forcetower.instrack.core.model.database.ProfileBond
import dev.forcetower.instrack.core.model.database.ProfileBondFollower
import dev.forcetower.instrack.core.model.database.ProfileBondFollowing
import dev.forcetower.instrack.core.model.database.ProfileFriendship
import dev.forcetower.instrack.core.model.ui.ProfileBondedSimple
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT PB.*, P.picture FROM ProfileBond AS PB INNER JOIN Profile P ON P.pk = PB.userPk WHERE PB.followsMe = 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1)")
    abstract fun getFollowers(): Flow<List<ProfileBondedSimple>>

    @Query("SELECT PB.*, P.picture FROM ProfileBond AS PB INNER JOIN Profile P ON P.pk = PB.userPk WHERE PB.followsMe = 0 AND PB.unfollowMeAt IS NOT NULL AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1)")
    abstract fun getUnfollowers(): Flow<List<ProfileBondedSimple>>

    @Query("SELECT PB.*, P.picture FROM ProfileBond PB INNER JOIN Profile P ON PB.userPk = p.pk WHERE PB.iFollow = 1 AND PB.followsMe IS NOT 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile LP WHERE LP.selected = 1 LIMIT 1)")
    abstract fun getNotFollowBack(): Flow<List<ProfileBondedSimple>>

    @Query("SELECT PB.*, P.picture FROM ProfileBond PB INNER JOIN Profile P ON PB.userPk = p.pk WHERE PB.followsMe = 1 AND PB.iFollow IS NOT 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile LP WHERE LP.selected = 1 LIMIT 1)")
    abstract fun getINotFollowBack(): Flow<List<ProfileBondedSimple>>

    @Query("SELECT PB.followsMeAt as timestamp, PB.followsMe, PB.iFollow, P.*, (SELECT 0) as insight FROM ProfileBond AS PB INNER JOIN Profile P ON P.pk = PB.userPk WHERE PB.followsMe = 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.followsMeAt > :reference ORDER BY PB.followsMeAt DESC")
    abstract fun getRecentFollowers(reference: Long): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.unfollowMeAt as timestamp, PB.followsMe, PB.iFollow, P.*, (SELECT 0) as insight FROM ProfileBond AS PB INNER JOIN Profile P ON P.pk = PB.userPk WHERE PB.followsMe = 0 AND PB.unfollowMeAt IS NOT NULL AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.unfollowMeAt > :reference ORDER BY PB.unfollowMeAt DESC")
    abstract fun getRecentUnfollowers(reference: Long): PagingSource<Int, UserFriendship>

    @Query("SELECT MAX(COALESCE(PB.unfollowMeAt, 0), COALESCE(PB.iFollowAt, 0)) as timestamp, PB.followsMe, PB.iFollow, P.*, (SELECT 0) as insight FROM ProfileBond AS PB INNER JOIN Profile P ON PB.userPk = p.pk WHERE PB.iFollow = 1 AND PB.followsMe IS NOT 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile LP WHERE LP.selected = 1 LIMIT 1) ORDER BY MAX(COALESCE(PB.unfollowMeAt, 0), COALESCE(PB.iFollowAt, 0)) DESC")
    abstract fun getUnrequited(): PagingSource<Int, UserFriendship>

    @Query("SELECT MAX(COALESCE(PB.unfollowMeAt, 0), COALESCE(PB.iFollowAt, 0)) as timestamp, PB.followsMe, PB.iFollow, P.*, (SELECT 0) as insight FROM ProfileBond AS PB INNER JOIN Profile P ON PB.userPk = p.pk WHERE PB.followsMe = 1 AND PB.iFollow IS NOT 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile LP WHERE LP.selected = 1 LIMIT 1) ORDER BY MAX(COALESCE(PB.unfollowMeAt, 0), COALESCE(PB.iFollowAt, 0)) DESC")
    abstract fun getFans(): PagingSource<Int, UserFriendship>
}

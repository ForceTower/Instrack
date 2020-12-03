package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Action
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ActionDao : BaseDao<Action>() {
    @Query("SELECT A.timestamp, COUNT(P.pk) as insight, COALESCE(PB.followsMe, 0) as followsMe, COALESCE(PB.iFollow, 0) as iFollow, P.* FROM `Action` A INNER JOIN Profile P ON p.pk = A.userPk LEFT JOIN ProfileBond PB ON A.userPk = PB.userPk WHERE A.userPk <> (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) AND A.profilePk = (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) GROUP BY P.pk ORDER BY COUNT(P.pk) DESC LIMIT 10")
    abstract fun getMostCompromised(): Flow<List<UserFriendship>>

    @Query("SELECT * FROM (SELECT A.timestamp, (SELECT 0) as insight, COALESCE(PB.followsMe, 0) as followsMe, COALESCE(PB.iFollow, 0) as iFollow, P.* FROM `Action` A INNER JOIN Profile P ON p.pk = A.userPk LEFT JOIN ProfileBond PB ON A.userPk = PB.userPk WHERE A.userPk <> (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) ORDER BY A.timestamp DESC) as X group by X.pk order by X.timestamp DESC")
    abstract fun getActions(): PagingSource<Int, UserFriendship>

    @Query("SELECT * FROM (SELECT A.timestamp, (SELECT 0) as insight, COALESCE(PB.followsMe, 0) as followsMe, COALESCE(PB.iFollow, 0) as iFollow, P.* FROM `Action` A INNER JOIN Profile P ON p.pk = A.userPk LEFT JOIN ProfileBond PB ON A.userPk = PB.userPk WHERE A.userPk <> (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) AND A.actionId = 5 ORDER BY A.timestamp DESC) as X group by X.pk order by X.timestamp DESC")
    abstract fun getStoryWatchersFriendship(): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, (SELECT 0) AS insight, (SELECT 0) AS timestamp, P.* FROM ProfileBond PB INNER JOIN Profile P ON PB.userPk = P.pk WHERE PB.followsMe = 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.userPk NOT IN (SELECT A.userPk FROM `Action` A WHERE A.profilePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND A.actionId IN (:actions))")
    abstract fun getFollowerNeverPerformAction(actions: List<Int>): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, (SELECT 0) AS insight, (SELECT 0) AS timestamp, P.* FROM ProfileBond PB INNER JOIN Profile P ON PB.userPk = P.pk WHERE PB.followsMe = 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.userPk NOT IN (SELECT A.userPk FROM `Action` A WHERE A.profilePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND A.actionId IN (3, 4, 6))")
    abstract fun getNeverLikedOrComment(): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, (SELECT 0) AS insight, (SELECT 0) AS timestamp, P.* FROM ProfileBond PB INNER JOIN Profile P ON PB.userPk = P.pk WHERE PB.followsMe = 1 AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.userPk NOT IN (SELECT A.userPk FROM `Action` A WHERE A.profilePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND A.actionId IN (3, 4, 5, 6))")
    abstract fun getNeverInteracted(): PagingSource<Int, UserFriendship>
}

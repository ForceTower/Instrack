package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Action
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class ActionDao : BaseDao<Action>() {
    @Query("SELECT * FROM (SELECT A.timestamp, COALESCE(PB.followsMe, 0) as followsMe, COALESCE(PB.iFollow, 0) as iFollow, P.* FROM `Action` A INNER JOIN Profile P ON p.pk = A.userPk INNER JOIN ProfileBond PB ON A.userPk = PB.userPk WHERE A.userPk <> (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) ORDER BY A.timestamp DESC) as X group by X.pk order by X.timestamp DESC")
    abstract fun getActions(): DataSource.Factory<Int, UserFriendship>

    @Query("SELECT * FROM (SELECT A.timestamp, COALESCE(PB.followsMe, 0) as followsMe, COALESCE(PB.iFollow, 0) as iFollow, P.* FROM `Action` A INNER JOIN Profile P ON p.pk = A.userPk INNER JOIN ProfileBond PB ON A.userPk = PB.userPk WHERE A.userPk <> (SELECT LP.userPK FROM LinkedProfile LP WHERE selected = 1) AND A.actionId = 5 ORDER BY A.timestamp DESC) as X group by X.pk order by X.timestamp DESC")
    abstract fun getStoryWatchersFriendship(): DataSource.Factory<Int, UserFriendship>
}
package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.instrack.core.model.database.PostComment
import dev.forcetower.instrack.core.model.ui.PostCommenterSimple
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostCommentDao : BaseDao<PostComment>() {
    @Query("SELECT * FROM PostComment WHERE pk = :pk")
    protected abstract suspend fun getCommentByIDDirect(pk: Long): PostComment?

    override suspend fun getValueByIDDirect(value: PostComment): PostComment? {
        return getCommentByIDDirect(value.pk)
    }

    @Query("SELECT COUNT(PC.pk) FROM PostComment AS PC INNER JOIN Post AS P ON PC.postPk = P.pk WHERE P.userPk = (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getCommentCount(): Flow<Int>

    @Query("SELECT PC.*, PR.picture FROM PostComment AS PC INNER JOIN Post AS P ON PC.postPk = P.pk INNER JOIN Profile PR on PC.userPk = PR.pk WHERE P.userPk = (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1) AND PC.userPk <> (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getCommenters(): Flow<List<PostCommenterSimple>>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(PC.userPk) AS insight, PC.timestamp AS timestamp, P.* FROM PostComment PC INNER JOIN ProfileBond PB ON PC.userPk = PB.userPk INNER JOIN Profile P ON PC.userPk = P.pk INNER JOIN Post PO ON PC.postPk = PO.pk WHERE PB.followsMe = 1 AND PO.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY PC.userPk ORDER BY COUNT(PC.userPk) DESC")
    abstract fun getUserMostComment(): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(PC.userPk) AS insight, PC.timestamp AS timestamp, P.* FROM PostComment PC INNER JOIN ProfileBond PB ON PC.userPk = PB.userPk INNER JOIN Profile P ON PC.userPk = P.pk INNER JOIN Post PO ON PC.postPk = PO.pk WHERE PB.followsMe = 1 AND PO.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY PC.userPk ORDER BY COUNT(PC.userPk) ASC")
    abstract fun getUserLeastComment(): PagingSource<Int, UserFriendship>

    @Query("SELECT COALESCE(PB.iFollow, 0) AS iFollow, COALESCE(PB.followsMe, 0) as followsMe, count(PC.userPk) AS insight, PC.timestamp AS timestamp, P.* FROM PostComment PC LEFT JOIN ProfileBond PB ON PC.userPk = PB.userPk INNER JOIN Profile P ON PC.userPk = P.pk INNER JOIN Post PO ON PC.postPk = PO.pk WHERE (PB.followsMe = 0 OR PB.followsMe IS NULL) AND PO.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PC.userPk <> (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY PC.userPk ORDER BY COUNT(PC.userPk) DESC")
    abstract fun getNotFollowerMostComment(): PagingSource<Int, UserFriendship>
}
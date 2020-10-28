package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.instrack.core.model.ui.PostLikerSimple
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostLikeDao : BaseDao<PostLike>() {
    @Query("SELECT * FROM PostLike WHERE postPk = :pk")
    abstract suspend fun getLikes(pk: Long): List<PostLike>

    @Query("SELECT COUNT(*) FROM PostLike AS PL INNER JOIN Post AS P ON PL.postPk = P.pk WHERE P.userPk = (SELECT userPk FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getLikesCount(): Flow<Int>

    @Query("SELECT PL.*, PR.picture FROM PostLike AS PL INNER JOIN Post AS P ON PL.postPk = P.pk INNER JOIN Profile PR on PL.userPk = PR.pk WHERE P.userPk = (SELECT userPk FROM LinkedProfile WHERE selected = 1 LIMIT 1) AND PL.userPk <> (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getLikesSimple(): Flow<List<PostLikerSimple>>

    @Query("SELECT * FROM PostLike WHERE userPk = :userPk AND postPk = :postPk")
    abstract suspend fun getPostLikeById(userPk: Long, postPk: Long): PostLike?

    override suspend fun getValueByIDDirect(value: PostLike): PostLike? {
        return getPostLikeById(value.userPk, value.postPk)
    }

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(PL.userPk) AS insight, PL.timestamp AS timestamp, P.* FROM PostLike PL INNER JOIN ProfileBond PB ON PL.userPk = PB.userPk INNER JOIN Profile P ON PL.userPk = P.pk INNER JOIN Post PO ON PL.postPk = PO.pk WHERE PB.followsMe = 1 AND PO.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY PL.userPk ORDER BY COUNT(PL.userPk) DESC")
    abstract fun getUserMostLikes(): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(PL.userPk) AS insight, PL.timestamp AS timestamp, P.* FROM PostLike PL INNER JOIN ProfileBond PB ON PL.userPk = PB.userPk INNER JOIN Profile P ON PL.userPk = P.pk INNER JOIN Post PO ON PL.postPk = PO.pk WHERE PB.followsMe = 1 AND PO.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY PL.userPk ORDER BY COUNT(PL.userPk) ASC")
    abstract fun getUserLeastLikes(): PagingSource<Int, UserFriendship>
}

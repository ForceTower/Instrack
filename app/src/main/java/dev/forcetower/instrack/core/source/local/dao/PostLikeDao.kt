package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.instrack.core.model.ui.PostLikerSimple
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
}

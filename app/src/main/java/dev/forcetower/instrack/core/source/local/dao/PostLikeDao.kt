package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostLikeDao : BaseDao<PostLike>() {
    @Query("SELECT * FROM PostLike WHERE postPk = :pk")
    abstract suspend fun getLikes(pk: Long): List<PostLike>

    @Query("SELECT COUNT(*) FROM PostLike AS PL INNER JOIN Post AS P ON PL.postPk = P.pk WHERE P.userPk = (SELECT userPk FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getLikesCount(): Flow<Int>
}

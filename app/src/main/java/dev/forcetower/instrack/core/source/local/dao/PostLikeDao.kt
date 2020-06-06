package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class PostLikeDao : BaseDao<PostLike>() {
    @Query("SELECT * FROM PostLike WHERE postPk = :pk")
    abstract suspend fun getLikes(pk: Long): List<PostLike>
}

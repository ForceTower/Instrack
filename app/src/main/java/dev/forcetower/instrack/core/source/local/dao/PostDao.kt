package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class PostDao : BaseDao<Post>() {
    @Query("SELECT * FROM Post WHERE pk = :pk")
    abstract suspend fun getPostById(pk: Long): Post?

    override suspend fun getValueByIDDirect(value: Post): Post? {
        return getPostById(value.pk)
    }
}
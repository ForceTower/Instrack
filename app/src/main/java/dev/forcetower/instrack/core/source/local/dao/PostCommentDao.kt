package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostComment
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class PostCommentDao : BaseDao<PostComment>() {
    @Query("SELECT * FROM PostComment WHERE pk = :pk")
    protected abstract suspend fun getCommentByIDDirect(pk: Long): PostComment?

    override suspend fun getValueByIDDirect(value: PostComment): PostComment? {
        return getCommentByIDDirect(value.pk)
    }
}
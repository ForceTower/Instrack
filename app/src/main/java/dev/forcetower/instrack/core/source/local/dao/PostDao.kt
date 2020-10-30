package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.model.ui.SimpleMedia
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class PostDao : BaseDao<Post>() {
    @Query("SELECT * FROM Post WHERE pk = :pk")
    abstract suspend fun getPostById(pk: Long): Post?

    override suspend fun getValueByIDDirect(value: Post): Post? {
        return getPostById(value.pk)
    }

    @Query("SELECT * FROM Post ORDER BY likeCount DESC")
    abstract fun getPostsByLike(): PagingSource<Int, Post>

    @Query("SELECT P.pk, P.id, P.code, P.mediaType, P.userPk, P.takenAt, P.caption, P.likeCount, P.viewCount, P.previewPicture, COUNT(PC.postPk) AS commentCount FROM Post P LEFT JOIN PostComment PC ON P.pk = PC.postPk GROUP BY P.pk ORDER BY COUNT(PC.postPk) DESC")
    abstract fun getPostsByComment(): PagingSource<Int, Post>

    @Query("SELECT * FROM Post ORDER BY viewCount DESC")
    abstract fun getPostsByView(): PagingSource<Int, Post>
}
package dev.forcetower.instrack.core.source.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    private val database: TrackDB
) {
    fun mostLikedMedias(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.post().getPostsByLike() }
        ).flow
    }

    fun mostCommentedMedias(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.post().getPostsByComment() }
        ).flow
    }
}

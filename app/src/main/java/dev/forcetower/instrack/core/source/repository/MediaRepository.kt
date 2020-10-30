package dev.forcetower.instrack.core.source.repository

import androidx.paging.PagingData
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.model.ui.SimpleMedia
import dev.forcetower.instrack.core.source.local.TrackDB
import dev.forcetower.toolkit.extensions.asPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    private val database: TrackDB
) {
    fun mostLikedMedias(): Flow<PagingData<Post>> {
        return database.post().getPostsByLike().asPager()
    }

    fun mostCommentedMedias(): Flow<PagingData<Post>> {
        return database.post().getPostsByComment().asPager()
    }
}
package dev.forcetower.instrack.view.engagement.media

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.source.repository.MediaRepository
import kotlinx.coroutines.flow.Flow

class MediaListingViewModel @ViewModelInject constructor(
    private val repository: MediaRepository
) : ViewModel() {
    private var mostLikes: Flow<PagingData<Post>>? = null
    private var mostComments: Flow<PagingData<Post>>? = null

    fun mostLikedMedias(): Flow<PagingData<Post>> {
        val source = mostLikes
        if (source != null) return source
        val next = repository.mostLikedMedias().cachedIn(viewModelScope)
        mostLikes = next
        return next
    }

    fun mostCommentedMedias(): Flow<PagingData<Post>> {
        val source = mostComments
        if (source != null) return source
        val next = repository.mostCommentedMedias().cachedIn(viewModelScope)
        mostComments = next
        return next
    }
}

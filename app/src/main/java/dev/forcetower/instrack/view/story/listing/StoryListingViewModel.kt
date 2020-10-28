package dev.forcetower.instrack.view.story.listing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class StoryListingViewModel @ViewModelInject constructor(
    private val repository: StoryRepository
) : ViewModel() {
    private var mostWatched: Flow<PagingData<StoryViewCount>>? = null
    private var leastWatched: Flow<PagingData<StoryViewCount>>? = null

    fun mostWatched(): Flow<PagingData<StoryViewCount>> {
        val source = mostWatched
        if (source != null) return source
        val next = repository.mostWatched().cachedIn(viewModelScope)
        mostWatched = next
        return next
    }

    fun leastWatched(): Flow<PagingData<StoryViewCount>> {
        val source = leastWatched
        if (source != null) return source
        val next = repository.leastWatched().cachedIn(viewModelScope)
        leastWatched = next
        return next
    }
}
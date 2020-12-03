package dev.forcetower.instrack.view.story.insight

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.StoryRepository
import dev.forcetower.toolkit.extensions.setValueIfNew

class StoryInsightViewModel @ViewModelInject constructor(
    private val repository: StoryRepository
) : ViewModel() {
    private var activeSource: LiveData<List<StoryViewCount>>? = null
    private var trackedSource: LiveData<List<StoryViewCount>>? = null
    private val _currentStory = MutableLiveData<Long>()

    val recentWatches = _currentStory.switchMap {
        repository.watchers(it).asLiveData(viewModelScope.coroutineContext)
    }

    val watcherFollower = _currentStory.switchMap {
        repository.watchersFollower(it).asLiveData(viewModelScope.coroutineContext)
    }

    val watcherNotFollower = _currentStory.switchMap {
        repository.watchersNotFollower(it).asLiveData(viewModelScope.coroutineContext)
    }

    val watcherNotFollow = _currentStory.switchMap {
        repository.watchersNotFollow(it).asLiveData(viewModelScope.coroutineContext)
    }

    fun active(): LiveData<List<StoryViewCount>> {
        val source = activeSource
        if (source != null) return source
        val next = repository.active().asLiveData(viewModelScope.coroutineContext)
        activeSource = next
        return next
    }

    fun tracked(): LiveData<List<StoryViewCount>> {
        val source = trackedSource
        if (source != null) return source
        val next = repository.inactive().asLiveData(viewModelScope.coroutineContext)
        trackedSource = next
        return next
    }

    fun setCurrentStory(pk: Long) {
        _currentStory.setValueIfNew(pk)
    }
}

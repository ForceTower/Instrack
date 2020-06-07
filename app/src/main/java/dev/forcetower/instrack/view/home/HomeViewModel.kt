package dev.forcetower.instrack.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.DataRepository
import dev.forcetower.instrack.core.source.repository.SyncRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    private val syncRepository: SyncRepository
) : ViewModel(), HomeActions {
    val profileOverview = repository.profileOverview().asLiveData()
    val homeElements = repository.homeElements().asLiveData(Dispatchers.IO)

    private val _refreshing = MediatorLiveData<Boolean>()
    override val refreshing: LiveData<Boolean> = _refreshing

    private val _onHomeElementClick = MutableLiveData<Event<HomeElement>>()
    val onHomeElementClick: LiveData<Event<HomeElement>> = _onHomeElementClick

    init {
        val source = repository.getSyncRegistry().asLiveData()
        _refreshing.addSource(source) {
            _refreshing.value = !(it?.isCompleted() ?: false)
        }
    }

    fun maybeSyncProfile() {
        viewModelScope.launch {
            syncRepository.maybeExecuteSelected()
        }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            syncRepository.executeSelected()
        }
    }

    override fun onHomeElementClick(element: HomeElement) {
        _onHomeElementClick.value = Event(element)
    }

    override fun onStoryClick(element: StoryViewCount) {
    }
}
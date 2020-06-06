package dev.forcetower.instrack.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.DataRepository
import dev.forcetower.instrack.core.source.repository.SyncRepository
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

    init {
        val source = repository.getSyncRegistry().asLiveData()
        _refreshing.addSource(source) {
            _refreshing.value = !(it?.isCompleted() ?: false)
        }
    }

    fun syncProfile() {
        viewModelScope.launch {
            syncRepository.executeSelected()
        }
    }

    override fun onRefresh() {
        syncProfile()
    }

    override fun onHomeElementClick(element: HomeElement) {
    }

    override fun onStoryClick(element: StoryViewCount) {
    }
}
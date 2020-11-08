package dev.forcetower.instrack.view.home

import androidx.hilt.lifecycle.ViewModelInject
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    repository: DataRepository,
    private val syncRepository: SyncRepository
) : ViewModel(), HomeActions {
    val profileOverview = repository.profileOverview().asLiveData()
    val homeElements = repository.homeElements().asLiveData(Dispatchers.IO)

    private val _refreshing = MediatorLiveData<Boolean>()
    override val refreshing: LiveData<Boolean> = _refreshing

    private val _onHomeElementClick = MutableLiveData<Event<HomeElement>>()
    val onHomeElementClick: LiveData<Event<HomeElement>> = _onHomeElementClick

    fun maybeSyncProfile() {
        Timber.d("Maybe calling sync?")
        viewModelScope.launch {
            syncRepository.maybeExecuteSelected()
        }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            _refreshing.value = true
            syncRepository.executeSelected()
            _refreshing.value = false
        }
    }

    override fun onHomeElementClick(element: HomeElement) {
        _onHomeElementClick.value = Event(element)
    }

    override fun onStoryClick(element: StoryViewCount) {
    }

    override fun onLogout() {
        Timber.d("Logout requested")
    }
}
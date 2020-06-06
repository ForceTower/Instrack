package dev.forcetower.instrack.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.DataRepository
import dev.forcetower.instrack.core.source.repository.SyncRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    private val syncRepository: SyncRepository
) : ViewModel(), HomeActions {

    val profileOverview = repository.profileOverview().asLiveData()

    fun syncProfile() {
        viewModelScope.launch {
            syncRepository.executeSelected()
        }
    }

    override fun onHomeElementClick(element: HomeElement) {
    }

    override fun onStoryClick(element: StoryViewCount) {
    }
}
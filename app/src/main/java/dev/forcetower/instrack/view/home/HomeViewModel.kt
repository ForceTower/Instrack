package dev.forcetower.instrack.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.source.repository.DataRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel(), HomeActions {

    val profileOverview = repository.profileOverview().asLiveData()

    override fun onHomeElementClick(element: HomeElement) {

    }

    override fun onStoryClick(element: StoryViewCount) {

    }
}
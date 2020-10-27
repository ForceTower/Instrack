package dev.forcetower.instrack.view.home

import androidx.lifecycle.LiveData
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount

interface HomeActions {
    val refreshing: LiveData<Boolean>
    fun onRefresh()
    fun onHomeElementClick(element: HomeElement)
    fun onStoryClick(element: StoryViewCount)
    fun onLogout()
}
package dev.forcetower.instrack.view.home

import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount

interface HomeActions {
    fun onHomeElementClick(element: HomeElement)
    fun onStoryClick(element: StoryViewCount)
}
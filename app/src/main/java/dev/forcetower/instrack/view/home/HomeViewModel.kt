package dev.forcetower.instrack.view.home

import androidx.lifecycle.ViewModel
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import javax.inject.Inject

class HomeViewModel @Inject constructor(

) : ViewModel(), HomeActions {
    override fun onHomeElementClick(element: HomeElement) {

    }

    override fun onStoryClick(element: StoryViewCount) {

    }
}
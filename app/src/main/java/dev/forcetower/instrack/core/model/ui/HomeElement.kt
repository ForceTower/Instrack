package dev.forcetower.instrack.core.model.ui

import android.content.Context
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.database.Profile

data class HomeElement (
    val stableId: Int,
    val total: Int,
    val new: Int,
    var newElements: List<Profile?>,
    val name: String,
    val loading: Boolean = false,
    val all: List<Profile> = emptyList()
) {
    init {
        if (newElements.size < 3) {
            newElements = newElements.toMutableList().apply {
                while (size < 3) {
                    add(null)
                }
            }
        }
    }
    companion object {
        fun buildLoadingList(context: Context): List<HomeElement> {
            return listOf(
                HomeElement(1, 0, 0, emptyList(), context.getString(R.string.home_element_new_followers), true),
                HomeElement(2, 0, 0, emptyList(), context.getString(R.string.home_element_unfollow), true),
                HomeElement(3, 0, 0, emptyList(), context.getString(R.string.home_element_profile_interaction), true),
                HomeElement(4, 0, 0, emptyList(), context.getString(R.string.home_element_not_follow_you_back), true),
                HomeElement(5, 0, 0, emptyList(), context.getString(R.string.home_element_story_watch), true),
                HomeElement(6, 0, 0, emptyList(), context.getString(R.string.home_element_you_dont_follow_back), true)
            )
        }
    }
}
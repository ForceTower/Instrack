package dev.forcetower.instrack.core.model.ui

import dev.forcetower.instrack.core.model.database.Profile

data class ProfileOverview(
    val user: Profile?,
    val picture: String?,
    val stories: List<StoryViewCount>,
    val carousel: List<HomeCarousel>
)
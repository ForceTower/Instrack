package dev.forcetower.instrack.core.model.ui

import androidx.room.Embedded
import dev.forcetower.instrack.core.model.database.Story

data class StoryViewCount(
    @Embedded
    val story: Story,
    val count: Int
)
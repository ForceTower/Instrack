package dev.forcetower.instrack.core.model.ui

data class StoryWatcherSimple (
    val picture: String?,
    val storyPk: Long,
    val userPk: Long,
    val timestamp: Long
)
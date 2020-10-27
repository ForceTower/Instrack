package dev.forcetower.instrack.core.model.ui

data class StoryWatchProfileSimple(
    val pk: Long,
    val name: String,
    val username: String,
    val picture: String?,
    val timestamp: Long
)
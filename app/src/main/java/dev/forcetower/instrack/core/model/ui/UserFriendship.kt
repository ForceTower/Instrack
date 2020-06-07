package dev.forcetower.instrack.core.model.ui

import androidx.room.Embedded
import dev.forcetower.instrack.core.model.database.Profile

data class UserFriendship(
    @Embedded
    val profile: Profile,
    val followsMe: Boolean,
    val iFollow: Boolean,
    val insight: Int,
    val timestamp: Long
)
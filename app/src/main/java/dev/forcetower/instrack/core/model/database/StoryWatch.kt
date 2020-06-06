package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.Index
import java.util.Calendar

@Entity(
    primaryKeys = ["storyPk", "userPk"],
    indices = [
        Index("userPk"),
        Index("storyPk")
    ]
)
data class StoryWatch(
    val storyPk: Long,
    val userPk: Long,
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
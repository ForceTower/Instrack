package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import java.util.Calendar

@Entity(primaryKeys = ["userPk", "postPk"])
data class PostLike(
    val userPk: Long,
    val postPk: Long,
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
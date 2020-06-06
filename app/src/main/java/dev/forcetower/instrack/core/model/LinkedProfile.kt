package dev.forcetower.instrack.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class LinkedProfile(
    @PrimaryKey(autoGenerate = false)
    val userPk: Long,
    val username: String,
    val password: String,
    val selected: Boolean = false,
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
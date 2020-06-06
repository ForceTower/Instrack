package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.UUID

@Entity
data class ProfileHistory(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val userPk: Long,
    val followerCount: Int,
    val followingCount: Int,
    val mediaCount: Int,
    val createdTime: Long = Calendar.getInstance().timeInMillis
)
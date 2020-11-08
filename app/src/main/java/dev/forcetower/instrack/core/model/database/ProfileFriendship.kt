package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity(primaryKeys = ["userPk", "followsPk", "date", "create"])
data class ProfileFriendship(
    val userPk: Long,
    val followsPk: Long,
    val create: Boolean, // true -> follow | false -> unfollow
    val date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
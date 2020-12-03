package dev.forcetower.instrack.core.model.ui

import java.util.Calendar

data class PostLikerSimple(
    val userPk: Long,
    val postPk: Long,
    val timestamp: Long = Calendar.getInstance().timeInMillis,
    val picture: String?
)

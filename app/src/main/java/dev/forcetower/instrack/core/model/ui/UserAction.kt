package dev.forcetower.instrack.core.model.ui

data class UserAction(
    val userPk: Long,
    val actionId: String,
    val picture: String?,
    val username: String,
    val name: String
)
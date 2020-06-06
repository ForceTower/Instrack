package dev.forcetower.instrack.core.model.ui

data class PostCommenterSimple (
    val pk: Long,
    val userPk: Long,
    val postPk: Long,
    val text: String,
    val likeCount: Int,
    val createdAt: Long,
    val timestamp: Long,
    val picture: String?
)
package dev.forcetower.instrack.core.model.ui

data class SimpleMedia(
    val pk: Long,
    val pictureUrl: String,
    val isVideo: Boolean,
    val isGallery: Boolean,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int
)

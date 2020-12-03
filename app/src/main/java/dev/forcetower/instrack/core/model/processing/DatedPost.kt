package dev.forcetower.instrack.core.model.processing

import java.time.LocalDateTime

data class DatedPost(
    val pk: Long,
    val id: String,
    val code: String,
    val mediaType: Int,
    val userPk: Long,
    val takenAt: LocalDateTime,
    val caption: String?,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val previewPicture: String?
)

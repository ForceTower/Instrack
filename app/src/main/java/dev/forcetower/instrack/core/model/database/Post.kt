package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forcetower.instagram.model.timeline.Item

@Entity
data class Post(
    @PrimaryKey(autoGenerate = false)
    val pk: Long,
    val id: String,
    val code: String,
    val mediaType: Int,
    val userPk: Long,
    val takenAt: Long,
    val caption: String?,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val previewPicture: String?
) {
    companion object {
        fun adapt(item: Item): Post {
            return Post(
                item.pk,
                item.id,
                item.code,
                item.mediaType,
                item.user.pk,
                item.takenAt,
                item.caption?.text,
                item.likeCount ?: 0,
                item.commentCount ?: 0,
                item.viewCount ?: 0,
                item.imageVersions2?.candidates?.get(0)?.url
            )
        }
    }
}
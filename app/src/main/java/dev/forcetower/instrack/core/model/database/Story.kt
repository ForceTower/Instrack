package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forcetower.instagram.model.timeline.Item

@Entity
data class Story (
    @PrimaryKey(autoGenerate = false)
    val pk: Long,
    val id: String,
    val code: String,
    val takenAt: Long,
    val mediaType: Int,
    val previewPicture: String?,
    val userPk: Long,
    val pictureUrl: String?,
    val videoUrl: String?,
    val videoDuration: Float?,
    val expiringAt: Long?
) {
    companion object {
        fun adapt(item: Item): Story {
            return Story(
                item.pk,
                item.id,
                item.code,
                item.takenAt,
                item.mediaType,
                item.imageVersions2?.candidates?.get(0)?.url,
                item.user.pk,
                item.imageVersions2?.candidates?.get(0)?.url,
                item.videoVersions?.get(0)?.url,
                item.videoDuration,
                item.expiringAt
            )
        }
    }
}
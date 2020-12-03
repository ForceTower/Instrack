package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forcetower.instagram.model.timeline.Item

@Entity
data class PostMedia(
    @PrimaryKey(autoGenerate = false)
    val pk: Long,
    val id: String,
    val postPk: Long,
    val mediaType: Int,
    val previewPicture: String?,
    val pictureUrl: String?,
    val videoUrl: String?,
    val videoDuration: Float?
) {
    companion object {
        fun extract(item: Item): List<PostMedia> {
            val medias = mutableListOf<PostMedia>()

            when (item.mediaType) {
                Item.PHOTO -> {
                    val media = PostMedia(
                        item.pk,
                        item.id,
                        item.pk,
                        item.mediaType,
                        item.imageVersions2?.candidates?.get(0)?.url,
                        item.imageVersions2?.candidates?.get(0)?.url,
                        null,
                        null
                    )
                    medias += media
                }
                Item.VIDEO -> {
                    val media = PostMedia(
                        item.pk,
                        item.id,
                        item.pk,
                        item.mediaType,
                        item.imageVersions2?.candidates?.get(0)?.url,
                        null,
                        item.videoVersions?.get(0)?.url,
                        item.videoDuration
                    )
                    medias += media
                }
                Item.CAROUSEL -> {
                    val carousel = item.carouselMedia?.map {
                        PostMedia(
                            it.pk,
                            it.id,
                            item.pk,
                            it.mediaType,
                            it.imageVersions2.candidates[0].url,
                            it.imageVersions2.candidates[0].url,
                            it.videoVersion?.get(0)?.url,
                            it.videoDuration
                        )
                    } ?: emptyList()
                    medias.addAll(carousel)
                }
            }

            return medias
        }
    }
}

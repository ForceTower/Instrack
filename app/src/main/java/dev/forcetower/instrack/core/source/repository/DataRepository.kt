package dev.forcetower.instrack.core.source.repository

import android.content.Context
import dev.forcetower.instrack.core.model.ui.HomeCarousel
import dev.forcetower.instrack.core.model.ui.ProfileOverview
import dev.forcetower.instrack.core.source.local.TrackDB
import dev.forcetower.toolkit.extensions.combine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val context: Context,
    private val database: TrackDB
) {
    fun profileOverview(): Flow<ProfileOverview> {
        val one = database.profile().getSelectedProfile()
        val two = database.postMedia().getLatestMedia()
        val three = database.story().getStoriesWithViewerCount()
        val four = database.postMedia().getVideosCount()
        val five = database.postMedia().getImageCount()
        val six = database.comment().getCommentCount()
        val seven = database.like().getLikesCount()
        return combine(one, two, three, four, five, six, seven) { profile, media, stories, videos, images, comments, likes ->
            val elements = HomeCarousel.createList(context, profile, videos, images, comments, likes)
            ProfileOverview(profile, media?.previewPicture, stories, elements)
        }
    }
}
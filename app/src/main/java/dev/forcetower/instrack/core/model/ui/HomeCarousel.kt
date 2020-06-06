package dev.forcetower.instrack.core.model.ui

import android.content.Context
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.database.Profile

data class HomeCarousel (
    val stableId: Int,
    val number: Int,
    val text: String
) {
    companion object {
        fun createList(
            context: Context,
            profile: Profile?,
            videos: Int,
            images: Int,
            comments: Int,
            likes: Int
        ): List<HomeCarousel> {
            val result = mutableListOf(
                HomeCarousel(10, videos, context.getString(R.string.home_carousel_videos_posted)),
                HomeCarousel(11, images, context.getString(R.string.home_carousel_images_posted)),
                HomeCarousel(12, comments, context.getString(R.string.home_carousel_comments_count)),
                HomeCarousel(13, likes, context.getString(R.string.home_carousel_likes_count))
            )
            if (profile != null) {
                result.add(0, HomeCarousel(0, profile.followerCount, context.getString(R.string.home_carousel_follower_count)))
                result.add(1, HomeCarousel(1, profile.followingCount, context.getString(R.string.home_carousel_following_count)))
                result.add(2, HomeCarousel(2, profile.mediaCount, context.getString(R.string.home_carousel_post_count)))
                if (profile.mediaCount != 0) {
                    result.add(3, HomeCarousel(3, likes / profile.mediaCount, context.getString(R.string.home_carousel_likes_mean)))
                    result.add(4, HomeCarousel(4, comments / profile.mediaCount, context.getString(R.string.home_carousel_comments_mean)))
                }
            }
            return result
        }
    }
}
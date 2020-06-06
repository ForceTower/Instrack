package dev.forcetower.instrack.core.source.repository

import android.content.Context
import androidx.room.withTransaction
import com.forcetower.instagram.Session
import dev.forcetower.instrack.core.model.database.Action
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.model.database.PostComment
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.instrack.core.model.database.PostMedia
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.model.database.ProfileBondFollower
import dev.forcetower.instrack.core.model.database.ProfileBondFollowing
import dev.forcetower.instrack.core.model.database.ProfileFriendship
import dev.forcetower.instrack.core.model.database.ProfileHistory
import dev.forcetower.instrack.core.model.database.ProfilePreview
import dev.forcetower.instrack.core.model.database.Story
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.instrack.core.model.database.SyncRegistry
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    private val context: Context,
    private val database: TrackDB
) {

    suspend fun executeSelected(): Long {
        val profile = database.profile().getSelectedProfileDirect()
        return if (profile != null) {
            execute(profile.username)
        } else {
            Timber.d("No selected profile... skipping")
            0
        }
    }

    suspend fun execute(username: String): Long {
        Timber.d("Execute for profile $username")
        val session = Session(username, context)
        val userId = session.account.userId
        Timber.d("Got user id: $userId")

        if (userId == null) {
            Timber.e("No user id detected... aborting")
            return 0
        }

        return withContext(Dispatchers.IO) {
            val syncId = database.sync().insertGettingId(SyncRegistry())
            val account = launch { account(session, userId) }
            val followers = launch { followers(session, userId) } // parallel
            val following = launch { following(session, userId) } // parallel
            val stories = launch { stories(session, userId) } // parallel
            val feed = launch { feed(session, userId) } // parallel

            account.invokeOnCompletion {
                database.sync().updateAccount(syncId, true, it != null)
                Timber.d("Account completed...")
            }
            followers.invokeOnCompletion {
                database.sync().updateFollowers(syncId, true, it != null)
                Timber.d("Followers completed")
            }
            following.invokeOnCompletion {
                database.sync().updateFollowing(syncId, true, it != null)
                Timber.d("Following completed")
            }
            stories.invokeOnCompletion {
                database.sync().updateStories(syncId, true, it != null)
                Timber.d("Stories completed")
            }
            feed.invokeOnCompletion {
                database.sync().updateFeed(syncId, true, it != null)
                Timber.d("Feed completed")
            }

            syncId
        }
    }

    private suspend fun account(session: Session, userId: Long) {
        val response = session.users.getInfoByUserId(userId)
        val data = response.data
        if (data != null && response.isSuccessful) {
            val profile = Profile.adapt(data.user)
            val history = ProfileHistory(userPk = profile.pk, followerCount = profile.followerCount, followingCount = profile.followingCount, mediaCount = profile.mediaCount)
            database.profile().insertOrUpdate(profile)
            database.history().insert(history)
        }
    }

    private suspend fun followers(session: Session, userId: Long) {
        var restart = true
        var hasMore: Boolean

        val local = database.bond().getFollowersSnapshot(userId)
        val server = mutableListOf<ProfilePreview>()

        do {
            val response = session.friendships.followers(userId, restart = restart)
            val data = response.data
            restart = false
            hasMore = response.isSuccessful && data?.nextMaxId != null
            if (data != null && response.isSuccessful) {
                val previews = data.users.map { ProfilePreview.adapt(it) }
                database.profile().insertOrUpdatePreview(previews)
                server.addAll(previews)
            }
            Timber.d("Server... $data")
        } while (hasMore)

        // differ
        // [a, b, c, d]
        val localPks = local.map { it.userPk }
        // [a, c, d, f]
        val serverPks = server.map { it.pk }

        Timber.d("Server PK $serverPks")
        Timber.d("Local PK $localPks")

        val instant = Calendar.getInstance().timeInMillis

        // [b]
        val unfollow = localPks.filter { !serverPks.contains(it) } // new unfollow list
        Timber.d("New unfollow list: $unfollow")
        val uUnfollow = unfollow.map { ProfileFriendship(it, userId, false) }
        val uBond = unfollow.map { ProfileBondFollower(it, userId, followsMe = false, unfollowMeAt = instant) }
        val uAction = unfollow.map { Action.unfollow(it, userId) }

        // [f]
        val follow = serverPks.filter { !localPks.contains(it) } // new follow list
        Timber.d("New follow list: $follow")
        val fFollow = follow.map { ProfileFriendship(it, userId, true) }
        val fBond = follow.map { ProfileBondFollower(it, userId, followsMe = true, followsMeAt = instant) }
        val fAction = follow.map { Action.follow(it, userId) }

        database.withTransaction {
            database.friendship().insertAll(uUnfollow + fFollow)
            database.bond().insertOrUpdateFollower(uBond + fBond)
            database.action().insertAllIgnore(uAction + fAction)
        }
    }

    private suspend fun following(session: Session, userId: Long) {
        var restart = true
        var hasMore: Boolean

        val local = database.bond().getFollowingSnapshot(userId)
        val server = mutableListOf<ProfilePreview>()

        do {
            val response = session.friendships.following(userId, restart = restart)
            val data = response.data
            restart = false
            hasMore = response.isSuccessful && data?.nextMaxId != null
            if (data != null && response.isSuccessful) {
                val previews = data.users.map { ProfilePreview.adapt(it) }
                database.profile().insertOrUpdatePreview(previews)
                server.addAll(previews)
            }
        } while (hasMore)

        // differ
        // [a, b, c, d]
        val localPks = local.map { it.followsPk }
        // [a, c, d, f]
        val serverPks = server.map { it.pk }

        val instant = Calendar.getInstance().timeInMillis

        // [b]
        val unfollow = localPks.filter { !serverPks.contains(it) } // new unfollow list
        val uUnfollow = unfollow.map { ProfileFriendship(userId, it, false) }
        val uBond = unfollow.map { ProfileBondFollowing(it, userId, iFollow = false, iUnfollowAt = instant) }
        // [f]
        val follow = serverPks.filter { !localPks.contains(it) } // new follow list
        val fFollow = follow.map { ProfileFriendship(userId, it, true) }
        val fBond = follow.map { ProfileBondFollowing(it, userId, iFollow = true, iFollowAt = instant) }

        database.withTransaction {
            database.friendship().insertAll(uUnfollow + fFollow)
            database.bond().insertOrUpdateFollowing(uBond + fBond)
        }
    }

    private suspend fun stories(session: Session, userId: Long) {
        // someone with X followers, watched your stories!
        val response = session.feeds.feedStory(userId)
        val data = response.data
        if (response.isSuccessful && data != null) {
            val items = data.reel?.items?.map { Story.adapt(it) } ?: return
            Timber.d("Stories fetched $items")
            database.story().insertAllIgnore(items)
            storiesAudience(session, items, userId)
        }
    }

    private suspend fun storiesAudience(session: Session, items: List<Story>, userId: Long) {
        withContext(Dispatchers.IO) {
            items.forEach { launch { fetchStoryAudience(session, it, userId) } }
        }
    }

    private suspend fun fetchStoryAudience(session: Session, story: Story, userId: Long) {
        var restart = true
        var hasMore: Boolean

        // TODO show screenshot'ers
        do {
            val response = session.stories.getStoryItemViewers(story.pk, restart)
            val data = response.data
            restart = false
            hasMore = response.isSuccessful && data?.nextMaxId != null
            if (data != null && response.isSuccessful) {
                database.profile().insertOrUpdatePreview(data.users.map { ProfilePreview.adapt(it) })
                database.storyWatch().insertAllIgnore(data.users.map { StoryWatch(story.pk, it.pk) })
                database.action().insertAllIgnore(data.users.map { Action.watch(it.pk, userId, story.pk) })
            }
        } while (hasMore)
    }

    private suspend fun feed(session: Session, userId: Long) {
        var restart = true
        var hasMore: Boolean

        val allPosts = mutableListOf<Post>()
        do {
            val response = session.feeds.feed(userId, restart)
            val data = response.data
            restart = false
            hasMore = response.isSuccessful && data?.nextMaxId != null
            if (data != null && response.isSuccessful) {
                val posts = data.items.map { Post.adapt(it) }
                database.post().insertAll(posts)
                val medias = data.items.flatMap { PostMedia.extract(it) }
                database.postMedia().insertAll(medias)
                allPosts += posts
            }
        } while (hasMore)
        postsAudience(session, allPosts.sortedByDescending { it.takenAt }.take(30), userId)
    }

    private suspend fun postsAudience(session: Session, posts: List<Post>, userId: Long) {
        posts.forEach {
            withContext(Dispatchers.IO) {
                launch { fetchPostAudience(session, it, userId) }
            }
        }
    }

    private suspend fun fetchPostAudience(session: Session, post: Post, userId: Long) {
        withContext(Dispatchers.IO) {
            launch { fetchPostLikes(session, post, userId) }
            launch { fetchPostComments(session, post, userId) }
        }
    }

    private suspend fun fetchPostLikes(session: Session, post: Post, userId: Long) {
        val response = session.medias.likers(post.id)
        val data = response.data
        if (response.isSuccessful && data != null) {
            val local = database.like().getLikes(post.pk)
            val serverPks = data.users.map { it.pk }
            val localPks = local.map { it.userPk }

            val dislike = localPks.filter { !serverPks.contains(it) }.map { PostLike(it, post.pk) }
            val like = serverPks.filter { !localPks.contains(it) }.map { PostLike(it, post.pk) }
            val likeActs = like.map { Action.like(it.userPk, userId, it.postPk) }
            val dislikeActs = dislike.map { Action.dislike(it.userPk, userId, it.postPk) }

            database.withTransaction {
                database.profile().insertOrUpdatePreview(data.users.map { ProfilePreview.adapt(it) })
                database.like().insertAll(like)
                database.like().delete(dislike)
                database.action().insertAllIgnore(likeActs + dislikeActs)
            }
        }
    }

    private suspend fun fetchPostComments(session: Session, post: Post, userId: Long) {
        var restart = true
        var hasMore: Boolean
        var leftCount = 4

        do {
            val response = session.medias.comments(post.id, restart = restart)
            val data = response.data
            restart = false
            hasMore = response.isSuccessful && data?.nextMaxId != null
            if (data != null && response.isSuccessful) {
                database.profile().insertOrUpdatePreview(data.comments.map { ProfilePreview.adapt(it.user) })
                database.comment().insertOrUpdate(data.comments.map { PostComment.adapt(it, post.pk) })
                database.action().insertAllIgnore(data.comments.map { Action.comment(it.user.pk, userId, it.pk) })
            }
            leftCount--
        } while (hasMore && leftCount > 0)
    }
}
package dev.forcetower.instrack.core.source.repository

import android.content.Context
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.database.SyncRegistry
import dev.forcetower.instrack.core.model.ui.HomeCarousel
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.PostCommenterSimple
import dev.forcetower.instrack.core.model.ui.PostLikerSimple
import dev.forcetower.instrack.core.model.ui.ProfileBondedSimple
import dev.forcetower.instrack.core.model.ui.ProfileOverview
import dev.forcetower.instrack.core.model.ui.StoryWatcherSimple
import dev.forcetower.instrack.core.source.local.TrackDB
import dev.forcetower.toolkit.extensions.combine
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val context: Context,
    private val database: TrackDB
) {
    fun getSyncRegistry(): Flow<SyncRegistry?> {
        return database.sync().getLatestSync()
    }

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

    private fun getFollowers() = database.bond().getFollowers()
    private fun getUnfollowers() = database.bond().getUnfollowers()
    private fun getWatchers() = database.storyWatch().getWatchers()
    private fun getComments() = database.comment().getCommenters()
    private fun getLikes() = database.like().getLikesSimple()
    private fun getNotFollowBack() = database.bond().getNotFollowBack()
    private fun getINotFollowBack() = database.bond().getINotFollowBack()

    fun homeElements(): Flow<List<HomeElement>> {
        val collection1 = getFollowers()
        val collection2 = getUnfollowers()
        val collection3 = getWatchers()
        val collection4 = getComments()
        val collection5 = getLikes()
        val collection6 = getNotFollowBack()
        val collection7 = getINotFollowBack()
        return combine(
            collection1,
            collection2,
            collection3,
            collection4,
            collection5,
            collection6,
            collection7
        ) { follow, unfollow, watchers, commenters, likers, notFollowBack, iNotFollowBack ->
            val (of, nf) = getProcessedFollowers(follow)
            val (ou, nu) = getProcessedUnfollowers(unfollow)
            val (ow, nw) = getProcessedWatchers(watchers)
            val (oc, nc) = getProcessedCommenters(commenters)
            val (ol, nl) = getProcessedLikers(likers)
            val (ob, nb) = getProcessedNotFollowBack(notFollowBack)
            val (oi, ni) = getProcessedINotFollowBack(iNotFollowBack)

            listOf(
                createNewFollowersItem(of, nf),
                createNewUnfollowerItem(ou, nu),
                createNewProfileInteraction(of, nf, ou, nu, ow, nw, oc, nc, ol, nl),
                createNotFollowBackItem(ob, nb),
                createNewWatchersItem(ow, nw),
                createINotFollowBackItem(oi, ni)
            )
        }
    }

    private fun createNewFollowersItem(old: List<ProfileBondedSimple>, new: List<ProfileBondedSimple>): HomeElement {
        return HomeElement(
            1,
            old.size + new.size,
            new.size,
            new.asSequence().sortedByDescending { it.followsMeAt }.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_new_followers)
        )
    }

    private fun createNewUnfollowerItem(old: List<ProfileBondedSimple>, new: List<ProfileBondedSimple>): HomeElement {
        return HomeElement(
            2,
            old.size + new.size,
            new.size,
            new.asSequence().sortedByDescending { it.unfollowMeAt }.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_unfollow)
        )
    }

    private fun createNewProfileInteraction(
        of: List<ProfileBondedSimple>,
        nf: List<ProfileBondedSimple>,
        ou: List<ProfileBondedSimple>,
        nu: List<ProfileBondedSimple>,
        ow: List<StoryWatcherSimple>,
        nw: List<StoryWatcherSimple>,
        oc: List<PostCommenterSimple>,
        nc: List<PostCommenterSimple>,
        ol: List<PostLikerSimple>,
        nl: List<PostLikerSimple>
    ): HomeElement {
        val f = nf.map { InternalTimedUser(it.userPk, it.picture, it.followsMeAt!!) }
        val u = nu.map { InternalTimedUser(it.userPk, it.picture, it.unfollowMeAt!!) }
        val w = nw.map { InternalTimedUser(it.userPk, it.picture, it.timestamp) }
        val c = nc.map { InternalTimedUser(it.userPk, it.picture, it.timestamp) }
        val l = nl.map { InternalTimedUser(it.userPk, it.picture, it.timestamp) }

        val t = f + u + w + c + l

        val sequence = t.asSequence().sortedByDescending { it.timestamp }.distinctBy { it.userPk }
        return HomeElement(
            3,
            (of.size + nf.size) + (ou.size + nu.size) + (ow.size + nw.size) + (oc.size + nc.size) + (ol.size + nl.size),
            t.size,
            sequence.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_profile_interaction),
            false,
            sequence.map { it.picture }.toList()
        )
    }

    private fun createNotFollowBackItem(old: List<ProfileBondedSimple>, new: List<ProfileBondedSimple>): HomeElement {
        val sequence = new.asSequence().sortedByDescending { it.unfollowMeAt }
        return HomeElement(
            4,
            old.size + new.size,
            new.size,
            sequence.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_not_follow_you_back),
            false,
            sequence.map { it.picture }.toList()
        )
    }

    private fun createNewWatchersItem(old: List<StoryWatcherSimple>, new: List<StoryWatcherSimple>): HomeElement {
        return HomeElement(
            5,
            old.size + new.size,
            new.size,
            new.asSequence().distinctBy { it.userPk }.sortedByDescending { it.timestamp }.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_story_watch)
        )
    }

    private fun createINotFollowBackItem(old: List<ProfileBondedSimple>, new: List<ProfileBondedSimple>): HomeElement {
        return HomeElement(
            6,
            old.size + new.size,
            new.size,
            new.asSequence().sortedByDescending { it.iUnfollowAt }.filter { it.picture != null }.take(3).map { it.picture }.toList().asReversed(),
            context.getString(R.string.home_element_you_dont_follow_back)
        )
    }

    private fun getProcessedFollowers(collection: List<ProfileBondedSimple>): Pair<List<ProfileBondedSimple>, List<ProfileBondedSimple>> {
        return collection.partitionByYesterday { it.followsMeAt!! }
    }

    private fun getProcessedUnfollowers(collection: List<ProfileBondedSimple>): Pair<List<ProfileBondedSimple>, List<ProfileBondedSimple>> {
        return collection.partitionByYesterday { it.unfollowMeAt!! }
    }

    private fun getProcessedWatchers(collection: List<StoryWatcherSimple>): Pair<List<StoryWatcherSimple>, List<StoryWatcherSimple>> {
        val distinct = collection.sortedByDescending { it.timestamp }
        return distinct.partitionByYesterday { it.timestamp }
    }

    private fun getProcessedCommenters(collection: List<PostCommenterSimple>): Pair<List<PostCommenterSimple>, List<PostCommenterSimple>> {
        val distinct = collection.sortedByDescending { it.timestamp }
        return distinct.partitionByYesterday { it.timestamp }
    }

    private fun getProcessedLikers(collection: List<PostLikerSimple>): Pair<List<PostLikerSimple>, List<PostLikerSimple>> {
        val distinct = collection.sortedByDescending { it.timestamp }
        return distinct.partitionByYesterday { it.timestamp }
    }

    private fun getProcessedNotFollowBack(collection: List<ProfileBondedSimple>): Pair<List<ProfileBondedSimple>, List<ProfileBondedSimple>> {
        return collection.partitionByYesterday { it.iFollowAt!! }
    }

    private fun getProcessedINotFollowBack(collection: List<ProfileBondedSimple>): Pair<List<ProfileBondedSimple>, List<ProfileBondedSimple>> {
        return collection.partitionByYesterday { it.followsMeAt!! }
    }
}

private fun <T> List<T>.partitionByYesterday(selector: (T) -> Long): Pair<List<T>, List<T>> {
    val edgeOfToday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }
    return partition { selector(it) < edgeOfToday.timeInMillis }
}

private data class InternalTimedUser(
    val userPk: Long,
    val picture: String?,
    val timestamp: Long
)

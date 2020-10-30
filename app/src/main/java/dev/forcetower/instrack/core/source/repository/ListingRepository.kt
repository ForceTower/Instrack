package dev.forcetower.instrack.core.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.toLiveData
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.local.TrackDB
import dev.forcetower.toolkit.extensions.asPager
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepository @Inject constructor(
    private val database: TrackDB
) {
    fun recentFollowers(): Flow<PagingData<UserFriendship>> {
        val edgeOfToday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return database.bond().getRecentFollowers(edgeOfToday).asPager()
    }

    fun recentUnfollowers(): Flow<PagingData<UserFriendship>> {
        val edgeOfToday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return database.bond().getRecentUnfollowers(edgeOfToday).asPager()
    }

    fun profileInteractions(): Flow<PagingData<UserFriendship>> {
        return database.action().getActions().asPager()
    }

    fun unrequitedFollowers(): Flow<PagingData<UserFriendship>> {
        return database.bond().getUnrequited().asPager()
    }

    fun storyWatchers(): Flow<PagingData<UserFriendship>> {
        return database.action().getStoryWatchersFriendship().asPager()
    }

    fun fans(): Flow<PagingData<UserFriendship>> {
        return database.bond().getFans().asPager()
    }
}
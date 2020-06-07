package dev.forcetower.instrack.core.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.local.TrackDB
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepository @Inject constructor(
    private val database: TrackDB
) {
    fun recentFollowers(): LiveData<PagedList<UserFriendship>> {
        val edgeOfToday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return database.bond().getRecentFollowers(edgeOfToday).toLiveData(20)
    }

    fun recentUnfollowers(): LiveData<PagedList<UserFriendship>> {
        val edgeOfToday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return database.bond().getRecentUnfollowers(edgeOfToday).toLiveData(20)
    }

    fun profileInteractions(): LiveData<PagedList<UserFriendship>> {
        return database.action().getActions().toLiveData(20)
    }

    fun unrequitedFollowers(): LiveData<PagedList<UserFriendship>> {
        return database.bond().getUnrequited().toLiveData(20)
    }

    fun storyWatchers(): LiveData<PagedList<UserFriendship>> {
        return database.action().getStoryWatchersFriendship().toLiveData(20)
    }

    fun fans(): LiveData<PagedList<UserFriendship>> {
        return database.bond().getFans().toLiveData(20)
    }
}
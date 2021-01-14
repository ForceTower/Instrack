package dev.forcetower.instrack.core.source.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.local.TrackDB
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

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.bond().getRecentFollowers(edgeOfToday) }
        ).flow
    }

    fun recentUnfollowers(): Flow<PagingData<UserFriendship>> {
        val edgeOfToday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.bond().getRecentUnfollowers(edgeOfToday) }
        ).flow
    }

    fun profileInteractions(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.action().getActions() }
        ).flow
    }

    fun unrequitedFollowers(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.bond().getUnrequited() }
        ).flow
    }

    fun storyWatchers(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.action().getStoryWatchersFriendship() }
        ).flow
    }

    fun fans(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.bond().getFans() }
        ).flow
    }
}

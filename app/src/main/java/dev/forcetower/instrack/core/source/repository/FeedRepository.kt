package dev.forcetower.instrack.core.source.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val database: TrackDB
) {
    fun usersMostLikes(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.like().getUserMostLikes() }
        ).flow
    }

    fun usersLeastLikes(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.like().getUserLeastLikes() }
        ).flow
    }

    fun usersMostComment(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.comment().getUserMostComment() }
        ).flow
    }

    fun usersLeastComment(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.comment().getUserLeastComment() }
        ).flow
    }

    fun usersNeverLikedOrComment(): Flow<PagingData<UserFriendship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.action().getNeverLikedOrComment() }
        ).flow
    }
}
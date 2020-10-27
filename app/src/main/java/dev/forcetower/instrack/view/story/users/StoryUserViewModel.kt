package dev.forcetower.instrack.view.story.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class StoryUserViewModel @ViewModelInject constructor(
    private val repository: StoryRepository
) : ViewModel() {
    private var greater: Flow<PagingData<UserFriendship>>? = null
    private var least: Flow<PagingData<UserFriendship>>? = null
    private var notFollower: Flow<PagingData<UserFriendship>>? = null

    fun greaterSpectators(): Flow<PagingData<UserFriendship>> {
        val source = greater
        if (source != null) return source
        val next = repository.greaterWatchers().cachedIn(viewModelScope)
        greater = next
        return next
    }

    fun leastSpectators(): Flow<PagingData<UserFriendship>> {
        val source = least
        if (source != null) return source
        val next = repository.leastWatchers().cachedIn(viewModelScope)
        least = next
        return next
    }

    fun notFollowerSpectators(): Flow<PagingData<UserFriendship>> {
        val source = notFollower
        if (source != null) return source
        val next = repository.notFollowersWatchers().cachedIn(viewModelScope)
        notFollower = next
        return next
    }
}
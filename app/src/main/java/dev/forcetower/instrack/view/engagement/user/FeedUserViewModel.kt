package dev.forcetower.instrack.view.engagement.user

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.repository.FeedRepository
import kotlinx.coroutines.flow.Flow

class FeedUserViewModel @ViewModelInject constructor(
    private val repository: FeedRepository
) : ViewModel() {
    private var mostLikes: Flow<PagingData<UserFriendship>>? = null
    private var leastLikes: Flow<PagingData<UserFriendship>>? = null
    private var mostComments: Flow<PagingData<UserFriendship>>? = null
    private var leastComments: Flow<PagingData<UserFriendship>>? = null
    private var neverLikedOrComment: Flow<PagingData<UserFriendship>>? = null
    private var neverInteracted: Flow<PagingData<UserFriendship>>? = null

    fun usersMostLikes(): Flow<PagingData<UserFriendship>> {
        val source = mostLikes
        if (source != null) return source
        val next = repository.usersMostLikes().cachedIn(viewModelScope)
        mostLikes = next
        return next
    }

    fun usersLeastLikes(): Flow<PagingData<UserFriendship>> {
        val source = leastLikes
        if (source != null) return source
        val next = repository.usersLeastLikes().cachedIn(viewModelScope)
        leastLikes = next
        return next
    }

    fun usersMostComment(): Flow<PagingData<UserFriendship>> {
        val source = mostComments
        if (source != null) return source
        val next = repository.usersMostComment().cachedIn(viewModelScope)
        mostComments = next
        return next
    }

    fun usersLeastComment(): Flow<PagingData<UserFriendship>> {
        val source = leastComments
        if (source != null) return source
        val next = repository.usersLeastComment().cachedIn(viewModelScope)
        leastComments = next
        return next
    }

    fun usersNeverLikedOrComment(): Flow<PagingData<UserFriendship>> {
        val source = neverLikedOrComment
        if (source != null) return source
        val next = repository.usersNeverLikedOrComment().cachedIn(viewModelScope)
        neverLikedOrComment = next
        return next
    }

    fun usersNeverInteracted(): Flow<PagingData<UserFriendship>> {
        val source = neverInteracted
        if (source != null) return source
        val next = repository.usersNeverInteracted().cachedIn(viewModelScope)
        neverInteracted = next
        return next
    }
}
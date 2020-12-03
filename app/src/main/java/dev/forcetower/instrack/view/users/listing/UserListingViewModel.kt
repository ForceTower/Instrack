package dev.forcetower.instrack.view.users.listing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dev.forcetower.instrack.core.source.repository.ListingRepository

class UserListingViewModel @ViewModelInject constructor(
    repository: ListingRepository
) : ViewModel() {
    val recentFollowers = repository.recentFollowers().cachedIn(viewModelScope)
    val recentUnfollowers = repository.recentUnfollowers().cachedIn(viewModelScope)
    val profileInteractions = repository.profileInteractions().cachedIn(viewModelScope)
    val unrequitedFollowers = repository.unrequitedFollowers().cachedIn(viewModelScope)
    val storyWatchers = repository.storyWatchers().cachedIn(viewModelScope)
    val fans = repository.fans().cachedIn(viewModelScope)
}

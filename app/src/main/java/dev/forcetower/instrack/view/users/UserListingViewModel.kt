package dev.forcetower.instrack.view.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.forcetower.instrack.core.source.repository.ListingRepository
import javax.inject.Inject

class UserListingViewModel @ViewModelInject constructor(
    repository: ListingRepository
) : ViewModel() {
    val recentFollowers = repository.recentFollowers()
    val recentUnfollowers = repository.recentUnfollowers()
    val profileInteractions = repository.profileInteractions()
    val unrequitedFollowers = repository.unrequitedFollowers()
    val storyWatchers = repository.storyWatchers()
    val fans = repository.fans()
}
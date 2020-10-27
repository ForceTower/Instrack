package dev.forcetower.instrack.view.users.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.databinding.FragmentUserListingBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class UserListingFragment : BaseFragment() {
    private lateinit var binding: FragmentUserListingBinding
    private val viewModel by viewModels<UserListingViewModel>()
    private val args by navArgs<UserListingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentUserListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.apply {
            title = createTitle(args.filterType)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val listingAdapter = UserListingAdapter()

        binding.recyclerUsers.apply {
            adapter = listingAdapter
            itemAnimator?.run {
                changeDuration = 0
            }
        }

        val source = createSource()
        source.observe(viewLifecycleOwner, Observer {
            listingAdapter.submitList(it)
        })
    }

    private fun createSource(): LiveData<PagedList<UserFriendship>> {
        return when (args.filterType) {
            1 -> viewModel.recentFollowers
            2 -> viewModel.recentUnfollowers
            3 -> viewModel.profileInteractions
            4 -> viewModel.unrequitedFollowers
            5 -> viewModel.storyWatchers
            6 -> viewModel.fans
            else -> throw IllegalArgumentException("Filter type ${args.filterType} is not valid")
        }
    }

    private fun createTitle(filterType: Int): String {
        val res = when (filterType) {
            1 -> R.string.listing_recent_followers
            2 -> R.string.listing_recent_unfollowers
            3 -> R.string.listing_interact_profile
            4 -> R.string.listing_unrequited_followers
            5 -> R.string.listing_stories_view
            6 -> R.string.listing_fans
            else -> R.string.listing_generic_title
        }
        return getString(res)
    }
}
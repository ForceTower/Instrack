package dev.forcetower.instrack.view.engagement.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentUserListingBinding
import dev.forcetower.toolkit.components.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedUserListingFragment : BaseFragment() {
    private lateinit var binding: FragmentUserListingBinding
    private lateinit var adapter: FeedUserAdapter
    private val viewModel by viewModels<FeedUserViewModel>()
    private val args by navArgs<FeedUserListingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentUserListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.apply {
            title = createTitle(args.type)
        }.root

        val resource = when (args.type) {
            1 -> R.plurals.likes_count
            2 -> R.plurals.comment_count
            3 -> R.plurals.likes_count
            4 -> R.plurals.comment_count
            5 -> null
            6 -> null
            7 -> R.plurals.likes_count
            8 -> R.plurals.comment_count
            else -> throw IllegalStateException("Type ${args.type} is not accepted")
        }
        adapter = FeedUserAdapter(resource)
        binding.recyclerUsers.apply {
            adapter = this@FeedUserListingFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0L
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = when (args.type) {
            1 -> viewModel.usersMostLikes()
            2 -> viewModel.usersMostComment()
            3 -> viewModel.usersLeastLikes()
            4 -> viewModel.usersLeastComment()
            5 -> viewModel.usersNeverLikedOrComment()
            6 -> viewModel.usersNeverInteracted()
            7 -> viewModel.usersNotFollowButLike()
            8 -> viewModel.usersNotFollowButComment()
            else -> throw IllegalStateException("Type ${args.type} is not accepted")
        }

        lifecycleScope.launch {
            source.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun createTitle(type: Int): String {
        return when(type) {
            1 -> getString(R.string.most_like_me)
            2 -> getString(R.string.most_comment_me)
            3 -> getString(R.string.least_like_me)
            4 -> getString(R.string.least_comment_me)
            5 -> getString(R.string.never_like_never_comment_me_title)
            6 -> getString(R.string.never_interacted)
            7 -> getString(R.string.discovery_not_follow_but_like)
            8 -> getString(R.string.discovery_not_follow_but_comment)
            else -> throw IllegalStateException("Type $type is not accepted")
        }
    }
}
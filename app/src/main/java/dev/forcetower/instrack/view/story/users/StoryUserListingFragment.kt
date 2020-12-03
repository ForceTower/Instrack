package dev.forcetower.instrack.view.story.users

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
class StoryUserListingFragment : BaseFragment() {
    private lateinit var binding: FragmentUserListingBinding
    private lateinit var adapter: StoryUserAdapter
    private val args by navArgs<StoryUserListingFragmentArgs>()
    private val viewModel by viewModels<StoryUserViewModel>()

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

        adapter = StoryUserAdapter()
        binding.recyclerUsers.apply {
            adapter = this@StoryUserListingFragment.adapter
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
            1 -> viewModel.greaterSpectators()
            2 -> viewModel.leastSpectators()
            3 -> viewModel.notFollowerSpectators()
            else -> throw IllegalStateException("Type ${args.type} is not accepted")
        }

        lifecycleScope.launch {
            source.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun createTitle(type: Int): String {
        return when (type) {
            1 -> getString(R.string.story_most_spectators)
            2 -> getString(R.string.story_least_spectators)
            3 -> getString(R.string.story_not_follow_spectators_title)
            else -> throw IllegalStateException("Type $type is not accepted")
        }
    }
}

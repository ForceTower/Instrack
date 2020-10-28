package dev.forcetower.instrack.view.story.listing

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
import dev.forcetower.instrack.databinding.FragmentStoryListingBinding
import dev.forcetower.toolkit.components.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryListingFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryListingBinding
    private lateinit var adapter: StoryAdapter
    private val viewModel by viewModels<StoryListingViewModel>()
    private val args by navArgs<StoryListingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentStoryListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        adapter = StoryAdapter()
        binding.recyclerStories.apply {
            adapter = this@StoryListingFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0L
            }
        }

        binding.title = when(args.type) {
            1 -> getString(R.string.story_most_watched_title)
            2 -> getString(R.string.story_least_watched_title)
            else -> throw IllegalStateException("Type ${args.type} is not allowed")
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = when(args.type) {
            1 -> viewModel.mostWatched()
            2 -> viewModel.leastWatched()
            else -> throw IllegalStateException("Type ${args.type} is not allowed")
        }

        lifecycleScope.launch {
            source.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
package dev.forcetower.instrack.view.users.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentSimpleSearchListingBinding
import dev.forcetower.instrack.view.story.insight.StoryInsightViewModel
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber

@AndroidEntryPoint
class SimpleSearchListingFragment : BaseFragment() {
    private lateinit var binding: FragmentSimpleSearchListingBinding
    private val viewModel: StoryInsightViewModel by activityViewModels()
    private lateinit var adapter: UserSimpleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = UserSimpleAdapter()
        val view = FragmentSimpleSearchListingBinding.inflate(inflater, container, false).also {
            binding = it
            binding.count = 0
        }.root

        binding.recyclerUsers.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = when(val type = requireArguments().getInt("type", 0)) {
            1 -> viewModel.recentWatches
            2 -> viewModel.recentWatches
            3 -> viewModel.watcherNotFollower
            4 -> viewModel.watcherFollower
            5 -> viewModel.watcherNotFollow
            else -> throw IllegalStateException("type $type is not accepted")
        }

        source.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.count = it.size
        })
    }

    companion object {
        fun instance(type: Int): SimpleSearchListingFragment {
            return SimpleSearchListingFragment().apply {
                arguments = bundleOf("type" to type)
            }
        }
    }
}
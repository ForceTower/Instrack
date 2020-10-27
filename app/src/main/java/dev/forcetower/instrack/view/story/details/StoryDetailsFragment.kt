package dev.forcetower.instrack.view.story.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.databinding.FragmentStoryDetailsBinding
import dev.forcetower.instrack.view.story.insight.StoryInsightViewModel
import dev.forcetower.instrack.view.users.listing.UserListingFragment
import dev.forcetower.instrack.view.users.simple.SimpleSearchListingFragment
import dev.forcetower.instrack.widget.ScaleLinearLayoutManager
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.getPixelsFromDp
import timber.log.Timber
import kotlin.reflect.typeOf

@AndroidEntryPoint
class StoryDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryDetailsBinding
    private lateinit var adapter: StoryAdapter
    private val viewModel: StoryInsightViewModel by activityViewModels()
    private val args by navArgs<StoryDetailsFragmentArgs>()

    private var firstInteraction = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  FragmentStoryDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.recyclerStories.apply {
            val width = resources.displayMetrics.widthPixels
            val next = width / 2 - requireContext().getPixelsFromDp(100).toInt() / 2
            setPadding(next, 0, next, 0)
        }

        adapter = StoryAdapter()
        binding.recyclerStories.adapter = adapter
        val snapper = PagerSnapHelper().apply {
            attachToRecyclerView(binding.recyclerStories)
        }

        val fragments = listOf(
            SimpleSearchListingFragment.instance(1) to getString(R.string.story_insight_most_watched),
            SimpleSearchListingFragment.instance(2) to getString(R.string.story_insight_recent),
            SimpleSearchListingFragment.instance(3) to getString(R.string.story_insight_not_follower),
            SimpleSearchListingFragment.instance(4) to getString(R.string.story_insight_following),
            SimpleSearchListingFragment.instance(5) to getString(R.string.story_insight_not_following)
        )

        val pager = FragmentPager(childFragmentManager, lifecycle, fragments)
        binding.pagerInsights.adapter = pager
        TabLayoutMediator(binding.tabLayout, binding.pagerInsights) { tab, position ->
            tab.text = fragments[position].second
        }.attach()

        binding.close.setOnClickListener { onBack() }

        binding.recyclerStories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val snapped = snapper.findSnapView(recyclerView.layoutManager) ?: return
                    val position = binding.recyclerStories.getChildAdapterPosition(snapped)
                    val element = adapter.currentList[position]
                    onElementSelected(element.pk)
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val source = if (args.type == 1) viewModel.active() else viewModel.tracked()
        source.observe(viewLifecycleOwner, {
            if (firstInteraction) {
                firstInteraction = false
                val initial = args.initialPosition
                if (initial >= 0) {
                    view.postDelayed({
                        binding.recyclerStories.layoutManager?.scrollToPosition(initial)
                        val list = adapter.currentList
                        if (initial < list.size) {
                            viewModel.setCurrentStory(list[initial].pk)
                        }
                    }, 500)
                }
            }
            adapter.submitList(it)
            view.postDelayed({
                (binding.recyclerStories.layoutManager as? ScaleLinearLayoutManager)?.middle()
            }, 100)
        })
    }

    private fun onElementSelected(pk: Long) {
        viewModel.setCurrentStory(pk)
    }

    private fun onBack() {
        findNavController().popBackStack()
    }

    private inner class FragmentPager(
        fm: FragmentManager,
        lifecycle: Lifecycle,
        val fragments: List<Pair<Fragment, String>>
    ) : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position].first
    }
}
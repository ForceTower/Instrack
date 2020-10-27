package dev.forcetower.instrack.view.story.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.databinding.FragmentStoryDetailsBinding
import dev.forcetower.instrack.view.users.listing.UserListingFragment
import dev.forcetower.instrack.view.users.simple.SimpleSearchListingFragment
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.getPixelsFromDp
import timber.log.Timber

@AndroidEntryPoint
class StoryDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryDetailsBinding
    private lateinit var adapter: StoryAdapter

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
        PagerSnapHelper().attachToRecyclerView(binding.recyclerStories)

        val fragments = listOf(
            SimpleSearchListingFragment() to getString(R.string.story_insight_most_watched),
            SimpleSearchListingFragment() to getString(R.string.story_insight_recent),
            SimpleSearchListingFragment() to getString(R.string.story_insight_not_follower),
            SimpleSearchListingFragment() to getString(R.string.story_insight_not_following)
        )

        val pager = FragmentPager(childFragmentManager, lifecycle, fragments)
        binding.pagerInsights.adapter = pager
        TabLayoutMediator(binding.tabLayout, binding.pagerInsights) { tab, position ->
            tab.text = fragments[position].second
        }.attach()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Submit list")

        adapter.submitList(listOf(
            StoryViewCount(1, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(2, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(3, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(4, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(5, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(6, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(7, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(8, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(9, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
            StoryViewCount(10, "https://images.unsplash.com/photo-1601758228006-964e41e5e8eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", 18235),
        ))
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
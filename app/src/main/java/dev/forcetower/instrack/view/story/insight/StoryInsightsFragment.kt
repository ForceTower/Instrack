package dev.forcetower.instrack.view.story.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.core.model.billing.PremiumStatus
import dev.forcetower.instrack.databinding.FragmentStoryInsightsBinding
import dev.forcetower.instrack.view.BillingViewModel
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class StoryInsightsFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryInsightsBinding
    private lateinit var activeAdapter: StoryInsightAdapter
    private lateinit var trackedAdapter: StoryInsightAdapter
    private val viewModel: StoryInsightViewModel by activityViewModels()
    private val billingViewModel by activityViewModels<BillingViewModel>()
    private lateinit var premiumStatus: PremiumStatus

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentStoryInsightsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        activeAdapter = StoryInsightAdapter()
        trackedAdapter = StoryInsightAdapter()

        binding.recyclerActiveStories.apply {
            adapter = activeAdapter
        }

        binding.recyclerTrackedStories.apply {
            adapter = trackedAdapter
        }

        binding.activeLayout.setOnClickListener { onNavigateToStoryInsights(1) }
        binding.recyclerActiveStories.setOnClickListener { onNavigateToStoryInsights(1) }
        binding.trackedLayout.setOnClickListener { onNavigateToStoryInsights(2) }
        binding.recyclerTrackedStories.setOnClickListener { onNavigateToStoryInsights(2) }

        binding.cardMostSpectators.setOnClickListener { onNavigateToStoryUsers(1) }
        binding.cardLeastSpectators.setOnClickListener { onNavigateToStoryUsers(2) }
        binding.cardNotFollowSpectators.setOnClickListener { onNavigateToStoryUsers(3) }

        binding.cardMostViewed.setOnClickListener { onNavigateToStoryListing(1) }
        binding.cardLeastViewed.setOnClickListener { onNavigateToStoryListing(2) }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.active().observe(
            viewLifecycleOwner,
            {
                activeAdapter.submitList(it)
            }
        )

        viewModel.tracked().observe(
            viewLifecycleOwner,
            {
                trackedAdapter.submitList(it)
            }
        )

        billingViewModel.premiumStatus.observe(
            viewLifecycleOwner,
            {
                premiumStatus = it
            }
        )
    }

    private fun moveOnEntitled(block: () -> Unit) {
        block()
//        if (::premiumStatus.isInitialized) {
//            if (premiumStatus.entitled) {
//                block()
//            } else {
//                findNavController().navigate(StoryInsightsFragmentDirections.actionGlobalPurchase())
//            }
//        }
    }

    private fun onNavigateToStoryInsights(type: Int) {
        moveOnEntitled {
            val directions = StoryInsightsFragmentDirections.actionStoryInsightsToStoryDetails(type)
            findNavController().navigate(directions)
        }
    }

    private fun onNavigateToStoryUsers(type: Int) {
        moveOnEntitled {
            val directions = StoryInsightsFragmentDirections.actionStoryInsightsToStoryUserListing(type)
            findNavController().navigate(directions)
        }
    }

    private fun onNavigateToStoryListing(type: Int) {
        moveOnEntitled {
            val directions = StoryInsightsFragmentDirections.actionStoryInsightsToStoryListing(type)
            findNavController().navigate(directions)
        }
    }
}

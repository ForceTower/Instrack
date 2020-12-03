package dev.forcetower.instrack.view.engagement.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.core.model.billing.PremiumStatus
import dev.forcetower.instrack.databinding.FragmentFeedInsightsBinding
import dev.forcetower.instrack.view.BillingViewModel
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class FeedInsightsFragment : BaseFragment() {
    private lateinit var binding: FragmentFeedInsightsBinding
    private val billingViewModel by activityViewModels<BillingViewModel>()
    private lateinit var premiumStatus: PremiumStatus

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentFeedInsightsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.cardMostLikes.setOnClickListener { onNavigateToUserListing(1) }
        binding.cardMostComments.setOnClickListener { onNavigateToUserListing(2) }

        binding.cardLeastLike.setOnClickListener { onNavigateToUserListing(3) }
        binding.cardLeastComment.setOnClickListener { onNavigateToUserListing(4) }
        binding.cardNeverLikeOrComment.setOnClickListener { onNavigateToUserListing(5) }
        binding.cardNeverInteracted.setOnClickListener { onNavigateToUserListing(6) }

        binding.cardNotFollowButLike.setOnClickListener { onNavigateToUserListing(7) }
        binding.cardNotFollowButComment.setOnClickListener { onNavigateToUserListing(8) }

        binding.cardMostLiked.setOnClickListener { onNavigateToMediaListing(1) }
        binding.cardMostCommented.setOnClickListener { onNavigateToMediaListing(2) }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//                findNavController().navigate(FeedInsightsFragmentDirections.actionGlobalPurchase())
//            }
//        }
    }

    private fun onNavigateToUserListing(type: Int) {
        moveOnEntitled {
            val directions = FeedInsightsFragmentDirections.actionMediaInsightsToFeedUserListing(type)
            findNavController().navigate(directions)
        }
    }

    private fun onNavigateToMediaListing(type: Int) {
        moveOnEntitled {
            val directions = FeedInsightsFragmentDirections.actionMediaInsightsToMediaListingInsight(type)
            findNavController().navigate(directions)
        }
    }
}

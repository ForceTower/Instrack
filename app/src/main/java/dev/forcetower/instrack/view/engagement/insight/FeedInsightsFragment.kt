package dev.forcetower.instrack.view.engagement.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentFeedInsightsBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class FeedInsightsFragment : BaseFragment() {
    private lateinit var binding: FragmentFeedInsightsBinding

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

        binding.cardMostLiked.setOnClickListener {  }
        binding.cardMostCommented.setOnClickListener {  }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun onNavigateToUserListing(type: Int) {
        val directions = FeedInsightsFragmentDirections.actionMediaInsightsToFeedUserListing(type)
        findNavController().navigate(directions)
    }
}
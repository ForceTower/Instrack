package dev.forcetower.instrack.view.story.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentStoryInsightsBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class StoryInsightsFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryInsightsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStoryInsightsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }
}
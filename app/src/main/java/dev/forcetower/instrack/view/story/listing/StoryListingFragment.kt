package dev.forcetower.instrack.view.story.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentStoryListingBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class StoryListingFragment : BaseFragment() {
    private lateinit var binding: FragmentStoryListingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStoryListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }
}
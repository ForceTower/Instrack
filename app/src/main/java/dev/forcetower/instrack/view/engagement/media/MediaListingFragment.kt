package dev.forcetower.instrack.view.engagement.media

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
import dev.forcetower.instrack.databinding.FragmentMediaListingBinding
import dev.forcetower.toolkit.components.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaListingFragment : BaseFragment() {
    private lateinit var binding: FragmentMediaListingBinding
    private lateinit var adapter: MediaAdapter
    private val viewModel by viewModels<MediaListingViewModel>()
    private val args by navArgs<MediaListingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentMediaListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.apply {
            title = when (args.type) {
                1 -> getString(R.string.most_liked_medias)
                2 -> getString(R.string.most_commented_medias)
                else -> throw IllegalStateException("Type ${args.type} is not accepted")
            }
        }.root

        adapter = MediaAdapter(args.type)

        binding.recyclerUsers.apply {
            adapter = this@MediaListingFragment.adapter
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
            1 -> viewModel.mostLikedMedias()
            2 -> viewModel.mostCommentedMedias()
            else -> throw IllegalStateException("Type ${args.type} is not accepted")
        }

        lifecycleScope.launch {
            source.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}

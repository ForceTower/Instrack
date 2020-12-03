package dev.forcetower.instrack.view.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentChallengeCodeBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class ChallengeCodeFragment : BaseFragment() {
    private lateinit var binding: FragmentChallengeCodeBinding

    private val viewModel by activityViewModels<ChallengeViewModel>()
    private val args by navArgs<ChallengeCodeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChallengeCodeBinding.inflate(inflater, container, false).also {
            binding = it
            it.label = args.label
            it.actions = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onComplete.observe(
            viewLifecycleOwner,
            EventObserver {
                onMoveToHome()
            }
        )
    }

    private fun onMoveToHome() {
        val directions = ChallengeCodeFragmentDirections.actionChallengeCodeToHome()
        findNavController().navigate(directions)
    }
}

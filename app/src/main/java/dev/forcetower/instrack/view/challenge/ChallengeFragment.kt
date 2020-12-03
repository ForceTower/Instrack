package dev.forcetower.instrack.view.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentChallengeBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class ChallengeFragment : BaseFragment() {
    private lateinit var binding: FragmentChallengeBinding
    private val viewModel by activityViewModels<ChallengeViewModel>()
    private val args by navArgs<ChallengeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.initChallenge(args.username, args.password)
        return FragmentChallengeBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ChallengeAdapter(viewModel)
        binding.recyclerOptions.adapter = adapter

        viewModel.options.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        viewModel.onNext.observe(
            viewLifecycleOwner,
            EventObserver {
                val directions = ChallengeFragmentDirections.actionChallengeToChallengeCode(it.label, it.value!!)
                findNavController().navigate(directions)
            }
        )
    }
}

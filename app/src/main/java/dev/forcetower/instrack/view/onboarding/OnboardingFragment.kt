package dev.forcetower.instrack.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentOnboardingBinding
import dev.forcetower.toolkit.components.BaseFragment

class OnboardingFragment : BaseFragment(), OnboardingActions {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOnboardingBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = this@OnboardingFragment
        }.root
    }

    override fun onMoveToLogin() {
        val directions = OnboardingFragmentDirections.actionOnboardingToLogin()
        val extras = FragmentNavigatorExtras(binding.image to getString(R.string.image_start_transition))
        findNavController().navigate(directions, extras)
    }
}
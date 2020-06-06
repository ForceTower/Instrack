package dev.forcetower.instrack.view.login

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentLoginBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.components.BaseViewModelFactory
import dev.forcetower.toolkit.lifecycle.EventObserver
import javax.inject.Inject

class LoginFragment : BaseFragment() {
    @Inject
    lateinit var factory: BaseViewModelFactory
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionSet()
            .addTransition(ChangeBounds().apply {
                pathMotion = ArcMotion()
            })
            .addTransition(ChangeTransform())
            .addTransition(ChangeClipBounds())
            .addTransition(ChangeImageTransform())
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .setInterpolator(FastOutSlowInInterpolator())

        sharedElementEnterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onLoginSuccess.observe(viewLifecycleOwner, EventObserver {
            showSnack(getString(R.string.instagram_connected_as, it.username))
            moveToHome()
        })
        viewModel.onLoginErrorMessage.observe(viewLifecycleOwner, EventObserver {
            showSnack(it, Snackbar.LENGTH_LONG)
        })
        viewModel.onLoginError.observe(viewLifecycleOwner, EventObserver {
            if (it == R.string.challenge_required) {
                // TODO move to challenge
            } else {
                showSnack(getString(it))
            }
        })
    }

    private fun moveToHome() {
        val directions = LoginFragmentDirections.actionLoginToHome()
        findNavController().navigate(directions)
    }
}
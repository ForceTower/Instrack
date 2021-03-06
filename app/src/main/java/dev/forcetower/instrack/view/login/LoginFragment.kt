package dev.forcetower.instrack.view.login

import android.os.Bundle
import android.transition.ArcMotion
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentLoginBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionSet()
            .addTransition(
                ChangeBounds().apply {
                    pathMotion = ArcMotion()
                }
            )
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
        viewModel.onLoginSuccess.observe(
            viewLifecycleOwner,
            EventObserver {
                showSnack(getString(R.string.instagram_connected_as, it.username))
                moveToHome()
            }
        )
        viewModel.onLoginErrorMessage.observe(
            viewLifecycleOwner,
            EventObserver {
                showSnack(it, Snackbar.LENGTH_LONG)
            }
        )
        viewModel.onLoginError.observe(
            viewLifecycleOwner,
            EventObserver {
                showSnack(getString(it))
            }
        )
        viewModel.onChallenge.observe(
            viewLifecycleOwner,
            EventObserver {
                moveToChallenge(it.first, it.second)
            }
        )
    }

    private fun moveToChallenge(username: String, password: String) {
        val directions = LoginFragmentDirections.actionLoginToChallenge(username, password)
        findNavController().navigate(directions)
    }

    private fun moveToHome() {
        val directions = LoginFragmentDirections.actionLoginToHome()
        findNavController().navigate(directions)
    }
}

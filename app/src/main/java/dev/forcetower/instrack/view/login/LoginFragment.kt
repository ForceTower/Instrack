package dev.forcetower.instrack.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.forcetower.instrack.databinding.FragmentLoginBinding
import dev.forcetower.toolkit.components.BaseFragment

class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }
}
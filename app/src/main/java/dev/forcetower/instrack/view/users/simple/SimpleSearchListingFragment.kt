package dev.forcetower.instrack.view.users.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.databinding.FragmentSimpleSearchListingBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class SimpleSearchListingFragment : BaseFragment() {
    private lateinit var binding: FragmentSimpleSearchListingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSimpleSearchListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }
}
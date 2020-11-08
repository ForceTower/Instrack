package dev.forcetower.instrack.view.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.core.model.billing.AugmentedSkuDetails
import dev.forcetower.instrack.databinding.FragmentPurchaseBinding
import dev.forcetower.instrack.view.BillingViewModel
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber

@AndroidEntryPoint
class PurchaseFragment : BaseFragment() {
    private val billingViewModel by activityViewModels<BillingViewModel>()
    private lateinit var binding: FragmentPurchaseBinding
    private lateinit var current: AugmentedSkuDetails

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentPurchaseBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.close.setOnClickListener { findNavController().popBackStack() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        billingViewModel.subscriptions.observe(viewLifecycleOwner, {
            Timber.d("List ${it.size} $it")
            if (it.isNotEmpty()) {
                current = it.first()
            }
        })

        binding.cardOption01.setOnClickListener {
            if (::current.isInitialized) billingViewModel.makePurchase(requireActivity(), current)
        }
    }
}
package dev.forcetower.instrack.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentHomeBinding
import dev.forcetower.instrack.databinding.HomeDrawerHeaderBinding
import dev.forcetower.instrack.view.BillingViewModel
import dev.forcetower.instrack.widget.ItemOffsetDecoration
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var header: HomeDrawerHeaderBinding
    private lateinit var adapter: HomeElementsAdapter
    private val viewModel by activityViewModels<HomeViewModel>()
    private val billingViewModel by activityViewModels<BillingViewModel>()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = HomeElementsAdapter(requireContext(), viewModel)
        val view =  FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawer,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawer.addDrawerListener(toggle)
        binding.toolbar.showContextMenu()
        binding.navigation.setDrawTopInsetForeground(false)

        val head = binding.navigation.getHeaderView(0)
        header = HomeDrawerHeaderBinding.bind(head)

        NavigationUI.setupWithNavController(binding.navigation, findNavController())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.maybeSyncProfile()
        }

        billingViewModel.premiumStatus.observe(viewLifecycleOwner, {
            Timber.d("Entitled Premium? $it")
        })

        binding.recyclerProfile.apply {
            val decoration = ItemOffsetDecoration(requireContext(), R.dimen.item_home_offset)
            adapter = this@HomeFragment.adapter
            setHasFixedSize(true)
            (layoutManager as GridLayoutManager).run {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) 2 else 1
                    }
                }
            }
            addItemDecoration(decoration)
            itemAnimator = null
        }

        viewModel.profileOverview.observe(viewLifecycleOwner, {
            adapter.headerData = it
            binding.username = it.user?.username
            header.profile = it.user
        })

        viewModel.homeElements.observe(viewLifecycleOwner, {
            adapter.elements = it
        })

        viewModel.onHomeElementClick.observe(viewLifecycleOwner, EventObserver {
//            val directions = HomeFragmentDirections.actionHomeToUserListing(it.stableId)
            val directions = HomeFragmentDirections.actionHomeToPurchase()
            findNavController().navigate(directions)
        })
    }
}
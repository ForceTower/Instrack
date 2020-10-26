package dev.forcetower.instrack.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentHomeBinding
import dev.forcetower.instrack.databinding.HomeDrawerHeaderBinding
import dev.forcetower.instrack.widget.ItemOffsetDecoration
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.components.BaseViewModelFactory
import dev.forcetower.toolkit.lifecycle.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var header: HomeDrawerHeaderBinding
    private lateinit var adapter: HomeElementsAdapter
    private val viewModel by viewModels<HomeViewModel>()

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


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.maybeSyncProfile()
        }

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
            val directions = HomeFragmentDirections.actionHomeToUserListing(it.stableId)
            findNavController().navigate(directions)
        })
    }
}
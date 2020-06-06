package dev.forcetower.instrack.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.FragmentHomeBinding
import dev.forcetower.instrack.widget.ItemOffsetDecoration
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.components.BaseViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    @Inject
    lateinit var factory: BaseViewModelFactory
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeElementsAdapter
    private val viewModel by viewModels<HomeViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = HomeElementsAdapter(requireContext(), viewModel)
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        viewModel.profileOverview.observe(viewLifecycleOwner, Observer {
            adapter.headerData = it
        })

        viewModel.homeElements.observe(viewLifecycleOwner, Observer {
            adapter.elements = it
        })

        if (savedInstanceState == null) {
            viewModel.syncProfile()
        }
    }
}
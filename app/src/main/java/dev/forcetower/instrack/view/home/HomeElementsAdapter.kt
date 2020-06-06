package dev.forcetower.instrack.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.HomeElement
import dev.forcetower.instrack.core.model.ui.ProfileOverview
import dev.forcetower.instrack.databinding.ItemHomeInfoBinding
import dev.forcetower.instrack.databinding.ItemHomeProfileBinding
import dev.forcetower.toolkit.extensions.inflate

class HomeElementsAdapter(
    private val context: Context,
    private val actions: HomeActions
) : RecyclerView.Adapter<HomeElementsAdapter.HomeHolder>() {
    private val differ = AsyncListDiffer(this,
        DiffCallback
    )
    var elements = HomeElement.buildLoadingList(context)
        set(value) {
            field = if (value.isEmpty()) HomeElement.buildLoadingList(context) else value
            differ.submitList(buildMergedList(values = field))
        }
    var headerData: ProfileOverview? = null
        set(value) {
            field = value
            differ.submitList(buildMergedList(header = field))
        }

    init {
        differ.submitList(buildMergedList(elements, headerData))
    }

    private fun buildMergedList(
        values: List<HomeElement> = elements,
        header: ProfileOverview? = headerData
    ): List<Any> {
        val result = mutableListOf<Any>()
        result += HeadElement(header)
        result.addAll(values)
        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return when (viewType) {
            R.layout.item_home_profile -> HomeHolder.Header(
                parent.inflate(viewType), actions
            )
            R.layout.item_home_info -> HomeHolder.Information(
                parent.inflate(viewType), actions
            )
            else -> throw IllegalStateException("No view defined for view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        when (holder) {
            is HomeHolder.Header -> {
                val element = differ.currentList[position] as HeadElement
                holder.binding.overview = element.data
                holder.adapterStories.submitList(element.data?.stories)
                holder.adapterCarousel.submitList(element.data?.carousel)
            }
            is HomeHolder.Information -> {
                val element = differ.currentList[position] as HomeElement
                holder.binding.element = element
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is HeadElement -> R.layout.item_home_profile
            is HomeElement -> R.layout.item_home_info
            else -> throw IllegalStateException("No valid view type found for position $position")
        }
    }

    sealed class HomeHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Information(val binding: ItemHomeInfoBinding, actions: HomeActions) : HomeHolder(binding.root) {
            init { binding.actions = actions }
        }

        class Header(val binding: ItemHomeProfileBinding, actions: HomeActions) : HomeHolder(binding.root) {
            val adapterStories = HomeStoriesAdapter(actions)
            val adapterCarousel = HomeCarouselAdapter()
            init {
                binding.recyclerStories.run {
                    adapter = adapterStories
                    itemAnimator?.let {
                        it.changeDuration = 0
                    }
                }
                binding.recyclerScrollingInformation.adapter = adapterCarousel
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is HeadElement && newItem is HeadElement -> true
                oldItem is HomeElement && newItem is HomeElement -> oldItem.stableId == newItem.stableId
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is HeadElement && newItem is HeadElement -> oldItem.data == newItem.data
                oldItem is HomeElement && newItem is HomeElement -> oldItem == newItem
                else -> false
            }
        }
    }

    private data class HeadElement(val data: ProfileOverview?)
}
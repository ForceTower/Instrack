package dev.forcetower.instrack.view.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.HomeCarousel
import dev.forcetower.instrack.databinding.ItemHomeScrollingInfoBinding
import dev.forcetower.toolkit.extensions.inflate

class HomeCarouselAdapter : ListAdapter<HomeCarousel, HomeCarouselAdapter.CarouselHolder>(
    DiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselHolder {
        return CarouselHolder(parent.inflate(R.layout.item_home_scrolling_info))
    }

    override fun onBindViewHolder(holder: CarouselHolder, position: Int) {
        holder.binding.item = getItem(position % currentList.size)
    }

    override fun getItemCount(): Int {
        return if (currentList.size != 0)
            Int.MAX_VALUE
        else
            super.getItemCount()
    }

    inner class CarouselHolder(val binding: ItemHomeScrollingInfoBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<HomeCarousel>() {
        override fun areItemsTheSame(oldItem: HomeCarousel, newItem: HomeCarousel) = oldItem.stableId == newItem.stableId
        override fun areContentsTheSame(oldItem: HomeCarousel, newItem: HomeCarousel) = oldItem == newItem
    }
}
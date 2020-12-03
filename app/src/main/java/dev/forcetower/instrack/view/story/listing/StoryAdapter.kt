package dev.forcetower.instrack.view.story.listing

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.databinding.ItemStoryListingBinding
import dev.forcetower.toolkit.extensions.inflate

class StoryAdapter : PagingDataAdapter<StoryViewCount, StoryAdapter.StoryHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.StoryHolder {
        return StoryHolder(parent.inflate(R.layout.item_story_listing))
    }

    override fun onBindViewHolder(holder: StoryAdapter.StoryHolder, position: Int) {
        holder.binding.element = getItem(position)
    }

    inner class StoryHolder(val binding: ItemStoryListingBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<StoryViewCount>() {
        override fun areItemsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem == newItem
        }
    }
}

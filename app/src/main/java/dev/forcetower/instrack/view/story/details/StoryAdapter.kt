package dev.forcetower.instrack.view.story.details

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.databinding.ItemStoryDetailsBinding
import dev.forcetower.toolkit.extensions.inflate

class StoryAdapter : ListAdapter<StoryViewCount, StoryAdapter.StoryHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        return StoryHolder(parent.inflate(R.layout.item_story_details))
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            element = item
        }
    }

    inner class StoryHolder(val binding: ItemStoryDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<StoryViewCount>() {
        override fun areItemsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem == newItem
        }
    }
}

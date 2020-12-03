package dev.forcetower.instrack.view.story.insight

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.databinding.ItemStoryMiniBinding
import dev.forcetower.toolkit.extensions.inflate

class StoryInsightAdapter : ListAdapter<StoryViewCount, StoryInsightAdapter.StoryHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryInsightAdapter.StoryHolder {
        return StoryHolder(parent.inflate(R.layout.item_story_mini))
    }

    override fun onBindViewHolder(holder: StoryInsightAdapter.StoryHolder, position: Int) {
        holder.binding.element = getItem(position)
    }

    inner class StoryHolder(val binding: ItemStoryMiniBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<StoryViewCount>() {
        override fun areItemsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: StoryViewCount, newItem: StoryViewCount): Boolean {
            return oldItem == newItem
        }
    }
}

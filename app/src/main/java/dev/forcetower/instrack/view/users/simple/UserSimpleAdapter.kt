package dev.forcetower.instrack.view.users.simple

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.StoryWatchProfileSimple
import dev.forcetower.instrack.databinding.ItemStorySimpleUserListingBinding
import dev.forcetower.toolkit.extensions.inflate

class UserSimpleAdapter : ListAdapter<StoryWatchProfileSimple, UserSimpleAdapter.SimpleHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSimpleAdapter.SimpleHolder {
        return SimpleHolder(parent.inflate(R.layout.item_story_simple_user_listing))
    }

    override fun onBindViewHolder(holder: UserSimpleAdapter.SimpleHolder, position: Int) {
        holder.binding.profile = getItem(position)
    }

    inner class SimpleHolder(val binding: ItemStorySimpleUserListingBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<StoryWatchProfileSimple>() {
        override fun areItemsTheSame(
            oldItem: StoryWatchProfileSimple,
            newItem: StoryWatchProfileSimple
        ): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(
            oldItem: StoryWatchProfileSimple,
            newItem: StoryWatchProfileSimple
        ): Boolean {
            return oldItem == newItem
        }
    }
}
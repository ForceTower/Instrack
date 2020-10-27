package dev.forcetower.instrack.view.story.users

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.databinding.ItemUserListingBinding
import dev.forcetower.toolkit.extensions.inflate

class StoryUserAdapter : PagingDataAdapter<UserFriendship, StoryUserAdapter.UserHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryUserAdapter.UserHolder {
        return UserHolder(parent.inflate(R.layout.item_user_listing))
    }

    override fun onBindViewHolder(holder: StoryUserAdapter.UserHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.element = item
        item?.let {
            binding.insight.text = binding.root.resources.getQuantityString(R.plurals.insight_stories_watched, item.insight, item.insight)
        }
    }

    inner class UserHolder(val binding: ItemUserListingBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<UserFriendship>() {
        override fun areItemsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem.profile.pk == newItem.profile.pk
        }

        override fun areContentsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem == newItem
        }
    }
}
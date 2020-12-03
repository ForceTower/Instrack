package dev.forcetower.instrack.view.engagement.user

import android.view.ViewGroup
import androidx.annotation.PluralsRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.databinding.ItemUserListingBinding
import dev.forcetower.toolkit.extensions.inflate

class FeedUserAdapter(
    @PluralsRes private val resource: Int?
) : PagingDataAdapter<UserFriendship, FeedUserAdapter.UserHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedUserAdapter.UserHolder {
        return UserHolder(parent.inflate(R.layout.item_user_listing))
    }

    override fun onBindViewHolder(holder: FeedUserAdapter.UserHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.element = item
        if (resource != null && item != null) {
            binding.insight.text = binding.root.resources.getQuantityString(resource, item.insight, item.insight)
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

package dev.forcetower.instrack.view.users.listing

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.databinding.ItemUserListingBinding
import dev.forcetower.toolkit.extensions.inflate

class UserListingAdapter : PagingDataAdapter<UserFriendship, UserListingAdapter.UserHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(parent.inflate(R.layout.item_user_listing))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.element = getItem(position)
    }

    inner class UserHolder(val binding: ItemUserListingBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallBack : DiffUtil.ItemCallback<UserFriendship>() {
        override fun areItemsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem.profile.pk == newItem.profile.pk
        }

        override fun areContentsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem == newItem
        }
    }
}

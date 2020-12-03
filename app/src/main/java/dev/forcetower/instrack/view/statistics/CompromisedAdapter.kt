package dev.forcetower.instrack.view.statistics

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.databinding.ItemMostCompromisedBinding
import dev.forcetower.toolkit.extensions.inflate

class CompromisedAdapter : ListAdapter<UserFriendship, CompromisedAdapter.UserHolder>(DiffCallBack) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompromisedAdapter.UserHolder {
        return UserHolder(parent.inflate(R.layout.item_most_compromised))
    }

    override fun onBindViewHolder(holder: CompromisedAdapter.UserHolder, position: Int) {
        holder.binding.element = getItem(position)
    }

    inner class UserHolder(val binding: ItemMostCompromisedBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallBack : DiffUtil.ItemCallback<UserFriendship>() {
        override fun areItemsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem.profile.pk == newItem.profile.pk
        }

        override fun areContentsTheSame(oldItem: UserFriendship, newItem: UserFriendship): Boolean {
            return oldItem == newItem
        }
    }
}

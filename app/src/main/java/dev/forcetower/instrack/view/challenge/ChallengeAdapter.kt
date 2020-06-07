package dev.forcetower.instrack.view.challenge

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.instagram.model.login.ChallengeOption
import dev.forcetower.instrack.R
import dev.forcetower.instrack.databinding.ItemChallengeOptionBinding
import dev.forcetower.toolkit.extensions.inflate

class ChallengeAdapter(
    private val actions: ChallengeActions
) : ListAdapter<ChallengeOption, ChallengeAdapter.OptionHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(parent.inflate(R.layout.item_challenge_option))
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.binding.option = getItem(position)
    }

    inner class OptionHolder(val binding: ItemChallengeOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ChallengeOption>() {
        override fun areItemsTheSame(oldItem: ChallengeOption, newItem: ChallengeOption): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areContentsTheSame(
            oldItem: ChallengeOption,
            newItem: ChallengeOption
        ): Boolean {
            return oldItem == newItem
        }
    }
}

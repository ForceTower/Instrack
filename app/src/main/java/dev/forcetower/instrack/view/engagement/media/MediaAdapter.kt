package dev.forcetower.instrack.view.engagement.media

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.model.ui.SimpleMedia
import dev.forcetower.instrack.databinding.ItemMediaWithInsightBinding
import dev.forcetower.toolkit.extensions.inflate

class MediaAdapter(
    private val insightType: Int
) : PagingDataAdapter<Post, MediaAdapter.MediaHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaAdapter.MediaHolder {
        return MediaHolder(parent.inflate(R.layout.item_media_with_insight))
    }

    override fun onBindViewHolder(holder: MediaAdapter.MediaHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            media = item
            type = insightType
            insight = if (insightType == 1) item?.likeCount else item?.commentCount
        }
    }

    inner class MediaHolder(val binding: ItemMediaWithInsightBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
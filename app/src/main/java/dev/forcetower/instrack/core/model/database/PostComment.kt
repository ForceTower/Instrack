package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forcetower.instagram.model.timeline.Comment
import java.util.Calendar

@Entity
data class PostComment(
    @PrimaryKey(autoGenerate = false)
    val pk: Long,
    val userPk: Long,
    val postPk: Long,
    val text: String,
    val likeCount: Int,
    val createdAt: Long,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) {
    companion object {
        fun adapt(comment: Comment, postPk: Long): PostComment {
            return PostComment(
                comment.pk,
                comment.user.pk,
                postPk,
                comment.text,
                comment.commentLikeCount,
                comment.createdAt
            )
        }
    }
}
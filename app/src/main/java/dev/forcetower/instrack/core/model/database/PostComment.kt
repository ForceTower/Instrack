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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostComment

        if (pk != other.pk) return false
        if (userPk != other.userPk) return false
        if (postPk != other.postPk) return false
        if (text != other.text) return false
        if (likeCount != other.likeCount) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pk.hashCode()
        result = 31 * result + userPk.hashCode()
        result = 31 * result + postPk.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + likeCount
        result = 31 * result + createdAt.hashCode()
        return result
    }

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
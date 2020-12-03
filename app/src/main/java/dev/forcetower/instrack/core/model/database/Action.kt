package dev.forcetower.instrack.core.model.database

import androidx.annotation.IntDef
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * User follow: 1
 * User unfollow: 2
 * User like: 3
 * User comment: 4
 * User watch story: 5
 */
@Entity
data class Action(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val userPk: Long,
    @ActionType
    val actionId: Int,
    val profilePk: Long,
    val referenceId: Long,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) {
    companion object {
        const val FOLLOW = 1
        const val UNFOLLOW = 2
        const val LIKE = 3
        const val COMMENT = 4
        const val WATCH_STORY = 5
        const val DISLIKE = 6

        private fun genFollowId(userPk: Long, receiverPk: Long) =
            "FOLLOW!!$userPk!!##!!$receiverPk!!FOLLOW"

        private fun genUnfollowId(userPk: Long, receiverPk: Long) =
            "UNFOLLOW!!$userPk!!##!!$receiverPk!!UNFOLLOW"

        private fun genLikeId(userPk: Long, receiverPk: Long, postPk: Long) =
            "LIKE!!$userPk!!$postPk!!$receiverPk!!LIKE"

        private fun genCommentId(userPk: Long, receiverPk: Long, commentPk: Long) =
            "COMMENT!!$userPk!!$commentPk!!$receiverPk!!COMMENT"

        private fun genWatchId(userPk: Long, receiverPk: Long, storyPk: Long) =
            "WATCH!!$userPk!!$storyPk!!$receiverPk!!WATCH"

        private fun genDislikeId(userPk: Long, receiverPk: Long, postPk: Long) =
            "DISLIKE!!$userPk!!$postPk!!$receiverPk!!DISLIKE"

        fun follow(userPk: Long, receiverPk: Long): Action {
            val id = genFollowId(userPk, receiverPk)
            return Action(id, userPk, FOLLOW, receiverPk, receiverPk)
        }

        fun unfollow(userPk: Long, receiverPk: Long): Action {
            val id = genUnfollowId(userPk, receiverPk)
            return Action(id, userPk, UNFOLLOW, receiverPk, receiverPk)
        }

        fun like(userPk: Long, receiverPk: Long, postPk: Long): Action {
            val id = genLikeId(userPk, receiverPk, postPk)
            return Action(id, userPk, LIKE, receiverPk, postPk)
        }

        fun comment(userPk: Long, receiverPk: Long, commentPk: Long): Action {
            val id = genCommentId(userPk, receiverPk, commentPk)
            return Action(id, userPk, COMMENT, receiverPk, commentPk)
        }

        fun watch(userPk: Long, receiverPk: Long, storyPk: Long): Action {
            val id = genWatchId(userPk, receiverPk, storyPk)
            return Action(id, userPk, WATCH_STORY, receiverPk, storyPk)
        }

        fun dislike(userPk: Long, receiverPk: Long, postPk: Long): Action {
            val id = genDislikeId(userPk, receiverPk, postPk)
            return Action(id, userPk, LIKE, receiverPk, postPk)
        }
    }
}

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.LOCAL_VARIABLE)
@IntDef(value = [Action.FOLLOW, Action.UNFOLLOW, Action.LIKE, Action.COMMENT, Action.WATCH_STORY, Action.DISLIKE])
annotation class ActionType

package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class SyncRegistry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val account: Boolean = false,
    val followers: Boolean = false,
    val following: Boolean = false,
    val stories: Boolean = false,
    val feed: Boolean = false,

    val accountError: String? = null,
    val followersError: String? = null,
    val followingError: String? = null,
    val storiesError: String? = null,
    val feedError: String? = null,

    val createdTime: Long = Calendar.getInstance().timeInMillis
) {
    fun isCompleted(): Boolean {
        val now = System.currentTimeMillis()

        return (account && followers && following && stories && feed) || (now - createdTime) > 600000
    }
}

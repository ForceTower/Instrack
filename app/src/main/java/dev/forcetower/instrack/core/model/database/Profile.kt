package dev.forcetower.instrack.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forcetower.instagram.model.user.Account
import com.forcetower.instagram.model.user.AccountPreview
import timber.log.Timber

@Entity
data class Profile (
    @PrimaryKey(autoGenerate = false)
    val pk: Long,
    val username: String,
    val name: String?,
    val picture: String?,
    val isPrivate: Boolean,
    val isVerified: Boolean,
    val followerCount: Int,
    val followingCount: Int,
    val mediaCount: Int,
    val biography: String?
) {
    companion object {
        fun adapt(account: Account): Profile {
            Timber.d("The account $account")
            return Profile(
                account.pk,
                account.username,
                account.fullName,
                account.profilePicUrl,
                account.private,
                account.verified,
                account.followerCount,
                account.followingCount,
                account.mediaCount,
                account.biography
            )
        }

        fun adapt(preview: ProfilePreview): Profile {
            return Profile(
                preview.pk,
                preview.username,
                preview.name,
                preview.picture,
                preview.isPrivate,
                preview.isVerified,
                0,
                0,
                0,
                null
            )
        }
    }
}

data class ProfilePreview (
    val pk: Long,
    val username: String,
    val name: String?,
    val picture: String?,
    val isPrivate: Boolean,
    val isVerified: Boolean
) {
    companion object {
        fun adapt(account: AccountPreview): ProfilePreview {
            return ProfilePreview(
                account.pk,
                account.username,
                account.fullName,
                account.profilePicUrl,
                account.private,
                account.verified
            )
        }
    }
}
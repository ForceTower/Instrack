package dev.forcetower.instrack.core.model.database

import androidx.room.Entity

@Entity(primaryKeys = ["userPk", "referencePk"])
data class ProfileBond(
    val userPk: Long,
    val referencePk: Long,
    val followsMe: Boolean? = null,
    val followsMeAt: Long? = null,
    val unfollowMeAt: Long? = null,
    val iFollow: Boolean? = null,
    val iFollowAt: Long? = null,
    val iUnfollowAt: Long? = null
) {
    companion object {
        @JvmStatic
        fun adapt(bond: ProfileBondFollower): ProfileBond {
            return ProfileBond(
                bond.userPk,
                bond.referencePk,
                followsMe = bond.followsMe,
                followsMeAt = bond.followsMeAt,
                unfollowMeAt = bond.unfollowMeAt
            )
        }

        @JvmStatic
        fun adapt(bond: ProfileBondFollowing): ProfileBond {
            return ProfileBond(
                bond.userPk,
                bond.referencePk,
                iFollow = bond.iFollow,
                iFollowAt = bond.iFollowAt,
                iUnfollowAt = bond.iUnfollowAt
            )
        }
    }
}

data class ProfileBondFollower(
    val userPk: Long,
    val referencePk: Long,
    val followsMe: Boolean? = null,
    val followsMeAt: Long? = null,
    val unfollowMeAt: Long? = null
)

data class ProfileBondFollowing(
    val userPk: Long,
    val referencePk: Long,
    val iFollow: Boolean? = null,
    val iFollowAt: Long? = null,
    val iUnfollowAt: Long? = null
)

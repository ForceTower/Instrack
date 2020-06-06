package dev.forcetower.instrack.core.model.ui

data class ProfileBondedSimple(
    val userPk: Long,
    val referencePk: Long,
    val followsMe: Boolean? = null,
    val followsMeAt: Long? = null,
    val unfollowMeAt: Long? = null,
    val iFollow: Boolean? = null,
    val iFollowAt: Long? = null,
    val iUnfollowAt: Long? = null,
    val picture: String? = null
)
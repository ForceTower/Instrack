package dev.forcetower.instrack.core.model.billing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PremiumStatus(
    val entitled: Boolean,
    @PrimaryKey
    val id: Int = 1
)
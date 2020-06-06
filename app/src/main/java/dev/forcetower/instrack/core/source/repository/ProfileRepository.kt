package dev.forcetower.instrack.core.source.repository

import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val database: TrackDB
) {
    fun getCurrentProfile() = database.linked().getSelected()
}
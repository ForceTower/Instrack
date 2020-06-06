package dev.forcetower.instrack.core.source.repository

import dev.forcetower.instrack.core.model.ui.ProfileOverview
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val database: TrackDB
) {
    fun profileOverview(): Flow<ProfileOverview> {
        val one = database.profile().getSelectedProfile()
        return one.map { ProfileOverview(it, null, listOf(), listOf()) }
    }
}
package dev.forcetower.instrack.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.forcetower.instrack.core.model.database.LinkedProfile
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.source.local.dao.LinkedProfileDao
import dev.forcetower.instrack.core.source.local.dao.ProfileDao

@Database(entities = [
    LinkedProfile::class,
    Profile::class
], version =  1)
abstract class TrackDB : RoomDatabase() {
    abstract fun linked(): LinkedProfileDao
    abstract fun profile(): ProfileDao
}
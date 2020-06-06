package dev.forcetower.instrack.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.forcetower.instrack.core.model.LinkedProfile
import dev.forcetower.instrack.core.source.local.dao.LinkedProfileDao

@Database(entities = [
    LinkedProfile::class
], version =  1)
abstract class TrackDB : RoomDatabase() {
    abstract fun linked(): LinkedProfileDao
}
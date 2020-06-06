package dev.forcetower.instrack.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.forcetower.instrack.core.source.local.TrackDB
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): TrackDB {
        return Room.databaseBuilder(context, TrackDB::class.java, "tracker.db").build()
    }
}
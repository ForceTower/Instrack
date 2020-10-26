package dev.forcetower.instrack.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcetower.instrack.core.source.local.TrackDB
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrackDB {
        return Room.databaseBuilder(context, TrackDB::class.java, "tracker.db").build()
    }
}
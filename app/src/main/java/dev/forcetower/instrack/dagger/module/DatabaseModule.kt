package dev.forcetower.instrack.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcetower.instrack.core.billing.BillingDB
import dev.forcetower.instrack.core.source.local.TrackDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrackDB {
        return Room.databaseBuilder(context, TrackDB::class.java, "tracker.db").build()
    }

    @Provides
    @Singleton
    fun provideBillingDatabase(@ApplicationContext context: Context): BillingDB {
        return Room.databaseBuilder(context, BillingDB::class.java, "billing.db").build()
    }
}

package dev.forcetower.instrack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.forcetower.instrack.utils.crashlytics.CrashlyticsTree
import timber.log.Timber

@HiltAndroidApp
class TrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // "Every time you log in production, a puppy dies"
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }
}

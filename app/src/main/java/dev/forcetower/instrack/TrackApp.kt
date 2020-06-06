package dev.forcetower.instrack

import dagger.android.support.DaggerApplication
import dev.forcetower.instrack.dagger.AppComponent
import dev.forcetower.instrack.dagger.DaggerAppComponent
import timber.log.Timber

class TrackApp : DaggerApplication() {
    private val component: AppComponent by lazy { createComponent() }

    override fun onCreate() {
        super.onCreate()
        // "Every time you log in production, a puppy dies"
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun applicationInjector() = component
}
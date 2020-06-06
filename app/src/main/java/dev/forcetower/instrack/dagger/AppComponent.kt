package dev.forcetower.instrack.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.forcetower.instrack.TrackApp
import dev.forcetower.instrack.dagger.module.ActivityModule
import dev.forcetower.instrack.dagger.module.AppModule
import dev.forcetower.instrack.dagger.module.DatabaseModule
import dev.forcetower.instrack.dagger.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    DatabaseModule::class,
    ActivityModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<TrackApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: TrackApp): Builder
        fun build(): AppComponent
    }
}
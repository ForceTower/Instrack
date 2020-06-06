package dev.forcetower.instrack.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.instrack.dagger.module.activity.MainActivityModule
import dev.forcetower.instrack.view.LauncherActivity
import dev.forcetower.instrack.view.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun launcher(): LauncherActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun main(): MainActivity
}
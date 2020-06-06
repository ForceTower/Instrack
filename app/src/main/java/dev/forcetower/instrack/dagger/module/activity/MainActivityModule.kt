package dev.forcetower.instrack.dagger.module.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.instrack.view.login.LoginFragment

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun login(): LoginFragment
}
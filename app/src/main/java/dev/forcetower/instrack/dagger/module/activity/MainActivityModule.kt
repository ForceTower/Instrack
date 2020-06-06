package dev.forcetower.instrack.dagger.module.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.instrack.view.login.LoginFragment
import dev.forcetower.instrack.view.onboarding.OnboardingFragment

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun onboarding(): OnboardingFragment
    @ContributesAndroidInjector
    abstract fun login(): LoginFragment
}
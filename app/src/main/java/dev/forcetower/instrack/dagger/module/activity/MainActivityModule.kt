package dev.forcetower.instrack.dagger.module.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.instrack.view.challenge.ChallengeCodeFragment
import dev.forcetower.instrack.view.challenge.ChallengeFragment
import dev.forcetower.instrack.view.home.HomeFragment
import dev.forcetower.instrack.view.login.LoginFragment
import dev.forcetower.instrack.view.onboarding.OnboardingFragment
import dev.forcetower.instrack.view.users.UserListingFragment

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun onboarding(): OnboardingFragment
    @ContributesAndroidInjector
    abstract fun login(): LoginFragment
    @ContributesAndroidInjector
    abstract fun challenge(): ChallengeFragment
    @ContributesAndroidInjector
    abstract fun challengeCode(): ChallengeCodeFragment
    @ContributesAndroidInjector
    abstract fun home(): HomeFragment
    @ContributesAndroidInjector
    abstract fun userListing(): UserListingFragment
}
package dev.forcetower.instrack.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.forcetower.instrack.view.challenge.ChallengeViewModel
import dev.forcetower.instrack.view.home.HomeViewModel
import dev.forcetower.instrack.view.launcher.LauncherViewModel
import dev.forcetower.instrack.view.login.LoginViewModel
import dev.forcetower.instrack.view.users.UserListingViewModel
import dev.forcetower.toolkit.components.BaseViewModelFactory
import dev.forcetower.toolkit.dagger.annotation.ViewModelKey

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindLauncherViewModel(vm: LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(vm: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeViewModel::class)
    abstract fun bindChallengeViewModel(vm: ChallengeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(vm: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserListingViewModel::class)
    abstract fun bindUserListingViewModel(vm: UserListingViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}
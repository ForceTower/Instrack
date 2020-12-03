package dev.forcetower.instrack.view.launcher

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.forcetower.instrack.core.source.repository.ProfileRepository
import dev.forcetower.toolkit.lifecycle.Event
import timber.log.Timber

class LauncherViewModel @ViewModelInject constructor(
    repository: ProfileRepository
) : ViewModel() {
    private val _launchDestination = MediatorLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>> = _launchDestination

    init {
        val source = repository.getCurrentProfile().asLiveData()
        _launchDestination.addSource(source) {
            _launchDestination.removeSource(source)
            Timber.d("$it")
            if (it == null) {
                _launchDestination.value = Event(LaunchDestination.LOGIN)
            } else {
                _launchDestination.value = Event(LaunchDestination.HOME)
            }
        }
    }
}

enum class LaunchDestination {
    HOME,
    LOGIN
}

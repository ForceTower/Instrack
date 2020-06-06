package dev.forcetower.instrack.view.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.toolkit.lifecycle.Event
import javax.inject.Inject

class LauncherViewModel @Inject constructor(

) : ViewModel() {
    private val _launchDestination = MediatorLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>> = _launchDestination

    init {
        _launchDestination.value = Event(LaunchDestination.LOGIN)
    }
}

enum class LaunchDestination {
    HOME,
    LOGIN
}
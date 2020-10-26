package dev.forcetower.instrack.view.challenge

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.forcetower.instagram.model.login.ChallengeOption
import dev.forcetower.instrack.core.source.repository.LoginRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChallengeViewModel @ViewModelInject constructor(
    private val repository: LoginRepository
) : ViewModel(), ChallengeActions {
    override val code = MutableLiveData("")

    private val _loadingOptions = MutableLiveData(false)
    override val loadingOptions: LiveData<Boolean> = _loadingOptions

    private val _loadingLogin = MutableLiveData(false)
    override val loadingLogin: LiveData<Boolean> = _loadingLogin

    private lateinit var challengeSource: LiveData<List<ChallengeOption>>
    private val _options = MediatorLiveData<List<ChallengeOption>>()
    val options: LiveData<List<ChallengeOption>> = _options

    private val _onNext = MutableLiveData<Event<ChallengeOption>>()
    val onNext: LiveData<Event<ChallengeOption>> = _onNext

    private val _onComplete = MutableLiveData<Event<Unit>>()
    val onComplete: LiveData<Event<Unit>> = _onComplete

    private var username: String = ""
    private var password: String = ""

    fun initChallenge(username: String, password: String) {
        if (!::challengeSource.isInitialized) {
            this.username = username
            this.password = password
            challengeSource = repository.challenge(username).asLiveData(Dispatchers.IO)
            _options.addSource(challengeSource) {
                _options.removeSource(challengeSource)
                _options.value = it
            }
        }
    }

    override fun onSelectAlternative(alternative: ChallengeOption) {
        _options.value?.map {
            if (it.value == alternative.value) {
                it.copy(selected = true)
            } else {
                it
            }
        }
    }

    override fun onMoveToNext() {
        val option = _options.value?.find { it.selected } ?: return
        viewModelScope.launch {
            _loadingOptions.value = true
            val result = repository.selectChallengeOption(username, option)
            _loadingOptions.value = false
            if (result) {
                _onNext.value = Event(option)
            }
        }
    }

    override fun onSendCode(code: String) {
        viewModelScope.launch {
            _loadingLogin.value = true
            val result = repository.sendChallengeCode(username, password, code)
            _loadingLogin.value = false
            if (result) {
                _onComplete.value = Event(Unit)
            }
        }
    }
}
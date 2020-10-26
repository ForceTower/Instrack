package dev.forcetower.instrack.view.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forcetower.instagram.model.user.Account
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.source.Operation
import dev.forcetower.instrack.core.source.repository.LoginRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository
) : ViewModel(), LoginActions {
    override val username = MutableLiveData("")
    override val password = MutableLiveData("")
    override val usernameError = MutableLiveData<Int>()
    override val passwordError = MutableLiveData<Int>()
    override val loading = MutableLiveData(false)

    private val _onLoginSuccess = MutableLiveData<Event<Account>>()
    val onLoginSuccess: LiveData<Event<Account>> = _onLoginSuccess

    private val _onLoginError = MutableLiveData<Event<Int>>()
    val onLoginError: LiveData<Event<Int>> = _onLoginError

    private val _onLoginErrorMessage = MutableLiveData<Event<String>>()
    val onLoginErrorMessage: LiveData<Event<String>> = _onLoginErrorMessage

    private val _onChallenge = MutableLiveData<Event<Pair<String, String>>>()
    val onChallenge: LiveData<Event<Pair<String, String>>> = _onChallenge

    override fun onLogin(username: String, password: String) {
        if (loading.value == true) return

        if (username.length < 3) {
            usernameError.value = R.string.login_username_too_short
            return
        }

        if (password.length < 3) {
            passwordError.value = R.string.login_password_too_short
            return
        }

        loading.value = true
        viewModelScope.launch {
            val result = repository.login(username, password)
            loading.value = false
            if (result is Operation.Success) {
                _onLoginSuccess.value = Event(result.data.loggedInUser!!)
            } else if (result is Operation.Error) {
                val data = result.data
                val errorType = result.data?.errorType
                val message = data?.message
                Timber.d("Error type $errorType")
                if (errorType != null && data != null) {
                    when {
                        errorType == "invalid_user" -> usernameError.value = R.string.invalid_user
                        errorType == "bad_password" -> passwordError.value = R.string.bad_password
                        errorType == "checkpoint_challenge_required" -> _onChallenge.value = Event(username to password)
                        message != null -> _onLoginErrorMessage.value = Event(message)
                        else -> _onLoginErrorMessage.value = Event(errorType)
                    }
                } else {
                    _onLoginError.value = Event(R.string.network_error)
                }
            }
        }
    }
}
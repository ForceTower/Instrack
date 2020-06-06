package dev.forcetower.instrack.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.instrack.R
import javax.inject.Inject

class LoginViewModel @Inject constructor(

) : ViewModel(), LoginActions {
    override val username = MutableLiveData("")
    override val password = MutableLiveData("")
    override val usernameError = MutableLiveData<Int>()
    override val passwordError = MutableLiveData<Int>()


    override fun onLogin(username: String, password: String) {
        if (username.length < 3) {
            usernameError.value = R.string.login_username_too_short
            return
        }

        if (password.length < 3) {
            passwordError.value = R.string.login_password_too_short
            return
        }
    }
}
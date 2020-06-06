package dev.forcetower.instrack.view.login

import androidx.lifecycle.MutableLiveData

interface LoginActions {
    val username: MutableLiveData<String>
    val password: MutableLiveData<String>

    val usernameError: MutableLiveData<Int>
    val passwordError: MutableLiveData<Int>

    fun onLogin(username: String, password: String)
}
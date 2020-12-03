package dev.forcetower.instrack.view.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forcetower.instagram.model.login.ChallengeOption

interface ChallengeActions {
    val code: MutableLiveData<String>
    val loadingOptions: LiveData<Boolean>
    val loadingLogin: LiveData<Boolean>

    fun onSelectAlternative(alternative: ChallengeOption)
    fun onMoveToNext()
    fun onSendCode(code: String)
}

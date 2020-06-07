package dev.forcetower.instrack.core.source.repository

import android.content.Context
import com.forcetower.instagram.Session
import com.forcetower.instagram.model.login.ChallengeOption
import com.forcetower.instagram.model.response.AccountResponse
import com.forcetower.instagram.model.response.InstagramResponse
import dev.forcetower.instrack.core.model.database.LinkedProfile
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.source.Operation
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val context: Context,
    private val database: TrackDB
) {
    private val sessions = mutableMapOf<String, Session>()
    suspend fun login(username: String, password: String): Operation<AccountResponse> {
        val session = sessions.getOrPut(username) { Session(username, context) }
        return try {
            val response = withContext(Dispatchers.IO) { session.account.login(password) }
            return onConnected(session, response, username, password)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "login error")
            Operation.error(throwable)
        }
    }

    fun challenge(username: String) = flow {
        val session = sessions.getOrPut(username) { Session(username, context) }
        emit(session.account.challenge())
    }

    suspend fun selectChallengeOption(username: String, option: ChallengeOption): Boolean = withContext(Dispatchers.IO) {
        val session = sessions.getOrPut(username) { Session(username, context) }
        try {
            val response = session.account.requestSecurityCode(option.value!!)
            Timber.d(">> code request ${response.peekBody(Long.MAX_VALUE).string()}")
            response.isSuccessful
        } catch (error: Throwable) {
            Timber.e(error, "Throwable... on challenge select option")
            false
        }
    }

    suspend fun sendChallengeCode(username: String, password: String, code: String): Boolean = withContext(Dispatchers.IO) {
        val session = sessions.getOrPut(username) { Session(username, context) }
        try {
            val response = session.account.submitSecurityCode(code)
            Timber.d(">> code send ${response.stringResponse}")
            onConnected(session, response, username, password)
            response.isSuccessful
        } catch (error: Throwable) {
            Timber.e(error, "Throwable... on challenge submit code")
            false
        }
    }

    private suspend fun onConnected(
        session: Session,
        response: InstagramResponse<AccountResponse>,
        username: String,
        password: String
    ): Operation<AccountResponse> {
        val data = response.data
        val user = response.data?.loggedInUser
        return if (response.isSuccessful && data != null && user != null) {
            database.linked().insertAndSelect(
                LinkedProfile(
                    user.pk,
                    username,
                    password,
                    true
                )
            )
            database.profile().insertOrUpdate(Profile.adapt(user))

            val me = withContext(Dispatchers.IO) { session.users.getInfoByUserId(user.pk) }.data?.user
            if (me != null) {
                database.profile().insertOrUpdate(Profile.adapt(me))
            }
            Operation.success(data, response.code)
        } else {
            Operation.error(Exception("login_failed"), response.code, data)
        }
    }
}
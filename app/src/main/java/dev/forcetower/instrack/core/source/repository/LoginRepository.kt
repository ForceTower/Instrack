package dev.forcetower.instrack.core.source.repository

import android.content.Context
import com.forcetower.instagram.Session
import com.forcetower.instagram.model.login.ChallengeOption
import com.forcetower.instagram.model.response.AccountResponse
import dev.forcetower.instrack.core.model.database.LinkedProfile
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.source.Operation
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.Dispatchers
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
            val data = response.data
            val user = response.data?.loggedInUser
            if (response.isSuccessful && data != null && user != null) {
                database.linked().insertAndSelect(
                    LinkedProfile(
                        user.pk,
                        username,
                        password,
                        true
                    )
                )
                database.profile().insertOrUpdate(Profile.adapt(user))

                val me = withContext(Dispatchers.IO) { session.users.getInfoByUserId(user.pk) }
                    .data?.user
                if (me != null) {
                    database.profile().insertOrUpdate(Profile.adapt(me))
                }

                Operation.success(data, response.code)
            } else {
                Operation.error(Exception("login_failed"), response.code, data)
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable, "login error")
            Operation.error(throwable)
        }
    }

    suspend fun challenge(username: String): List<ChallengeOption> {
        val session = sessions.getOrPut(username) { Session(username, context) }
        return withContext(Dispatchers.IO) { session.account.challenge() }
    }
}
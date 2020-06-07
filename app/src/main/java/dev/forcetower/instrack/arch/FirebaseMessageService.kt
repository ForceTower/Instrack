package dev.forcetower.instrack.arch

import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New token! $token")
    }
}
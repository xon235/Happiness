package com.family.happiness.service

import com.family.happiness.HappinessApplication
import com.family.happiness.network.request.RegisterFcmData
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        runBlocking { (application as HappinessApplication).userRepository.insertFcmToken(token) }
    }
}
package com.family.happiness.network.request

data class SignInData(
    val fcmToken: String,
    val oAuthData: OAuthData
)
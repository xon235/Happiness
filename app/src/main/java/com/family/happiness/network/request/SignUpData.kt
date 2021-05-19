package com.family.happiness.network.request

data class SignUpData(
    val oAuthData: OAuthData,
    val phone: String,
    val code: String,
)
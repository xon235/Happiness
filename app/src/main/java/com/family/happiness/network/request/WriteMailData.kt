package com.family.happiness.network.request

data class WriteMailData(
    val toUserId: String,
    val content: String
)
package com.family.happiness.network.request

data class MarkAsReadData(
    val mailId: Int,
    val rating: Float
)
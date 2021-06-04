package com.family.happiness.network.request

data class MovePhotoData(
    val photoUrls: List<String>,
    val eventId: Int
)
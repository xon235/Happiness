package com.family.happiness.room.photo

data class PhotoWithEvent(
    val url: String,
    val userId: String?,
    val eventId: String,
    val photoTimestamp: String,
    val eventName: String,
    val eventTimestamp: String,
)
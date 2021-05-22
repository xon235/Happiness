package com.family.happiness.network.response

import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo

data class UploadPhotosResponse(
    val events: List<Event>?,
    val photos: List<Photo>?,
)
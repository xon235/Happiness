package com.family.happiness.network.response

import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.tag.Tag

data class SyncPhotoResponse(
    val events: List<Event>,
    val tags: List<Tag>,
    val photos: List<Photo>
)
package com.family.happiness.room.photo

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.event.Event
import com.family.happiness.room.tag.Tag
import com.family.happiness.room.tag.TagDetail
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoDetail (
    @Embedded
    val photo: Photo,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: User,
    @Relation(
        parentColumn = "event_id",
        entityColumn = "id"
    )
    val event: Event,
    @Relation(
        entity = Tag::class,
        parentColumn = "event_id",
        entityColumn = "event_id"
    )
    val tagDetails: List<TagDetail>
): Parcelable
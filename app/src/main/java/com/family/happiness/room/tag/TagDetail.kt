package com.family.happiness.room.tag

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagDetail(
    @Embedded
    val tag: Tag,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: User
): Parcelable
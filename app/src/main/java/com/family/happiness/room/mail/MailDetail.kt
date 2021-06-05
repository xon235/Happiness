package com.family.happiness.room.mail

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class MailDetail(
    @Embedded
    val mail: Mail,
    @Relation(
        parentColumn = "from_user_id",
        entityColumn = "id"
    )
    val fromUser: User,
): Parcelable
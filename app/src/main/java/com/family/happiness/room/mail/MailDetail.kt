package com.family.happiness.room.mail

import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.user.User

data class MailDetail(
    @Embedded
    val mail: Mail,
    @Relation(
        parentColumn = "from_user_id",
        entityColumn = "id"
    )
    val fromUser: User,
)
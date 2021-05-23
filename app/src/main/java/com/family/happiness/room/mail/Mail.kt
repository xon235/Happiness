package com.family.happiness.room.mail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.family.happiness.room.user.User

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["from_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Mail(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "from_user_id") val fromUserId: String
)
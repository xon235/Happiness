package com.family.happiness.room.wish

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Wish(
    @PrimaryKey val id: String,
    val user_id: String,
    val title: String,
    val content: String,
    @ColumnInfo(name = "timestamp_open") val timestampOpen: String,
    @ColumnInfo(name = "timestamp_close") val timestampClose: String?,
) : Parcelable
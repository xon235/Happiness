package com.family.happiness.room.wish

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.family.happiness.room.user.User
import com.squareup.moshi.Json
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
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id") val userId: String,
    val title: String,
    val content: String,
    @ColumnInfo(name = "timestamp_open") val timestampOpen: String,
    @ColumnInfo(name = "timestamp_close") val timestampClose: String?,
) : Parcelable
package com.family.happiness.room.tag

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.family.happiness.room.event.Event
import com.family.happiness.room.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Tag(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id") val userId: String?,
    @ColumnInfo(name = "event_id") val eventId: String,
) : Parcelable
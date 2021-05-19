package com.family.happiness.room.contributor

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.family.happiness.room.user.User
import com.family.happiness.room.wish.Wish
import kotlinx.parcelize.Parcelize

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
            entity = Wish::class,
            parentColumns = ["id"],
            childColumns = ["wish_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Contributor(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id") val userId: String?,
    @ColumnInfo(name = "wish_id") val wishId: String,
) : Parcelable
package com.family.happiness.room.user

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String,
): Parcelable
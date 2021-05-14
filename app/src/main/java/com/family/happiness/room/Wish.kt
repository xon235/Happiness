package com.family.happiness.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Wish(
    @PrimaryKey val id: String,
    val user_id: String,
    val title: String,
    val content: String,
    val timestamp_create: String,
    val timestamp_finish: String?,
):Parcelable
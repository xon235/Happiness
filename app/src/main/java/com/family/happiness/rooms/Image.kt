package com.family.happiness.rooms

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Image(
        @PrimaryKey val url: String,
        val id_user: String,
        val album: String,
        val timestamp: String,
):Parcelable
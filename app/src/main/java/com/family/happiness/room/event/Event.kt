package com.family.happiness.room.event

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Event(
    @PrimaryKey val id: Int,
    val name: String,
    val timestamp: String,
) : Parcelable
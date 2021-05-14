package com.family.happiness.room

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["user_id", "wish_id"])
data class Contributor(
    val user_id: String,
    val wish_id: String,
):Parcelable
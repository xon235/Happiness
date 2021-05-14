package com.family.happiness.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Member(
        @PrimaryKey val id: String,
        val name: String,
        val phone: String,
        val photoUrl: String)
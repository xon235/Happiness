package com.family.happiness.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.family.happiness.network.AuthData

@Entity(indices = [Index(value=["id_family"], unique=true)])
data class User(
        @PrimaryKey val id: String,
        val session: String,
        val name: String,
        val phone: String,
        val photo_url: String,
        val id_family: String?
        )

fun User.toAuthData(): AuthData{
        return AuthData(
                id,
                session
        )
}

fun User.toMember(): Member{
        return Member(
                id,
                name,
                phone,
                photo_url
        )
}
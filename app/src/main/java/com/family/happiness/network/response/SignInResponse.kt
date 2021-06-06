package com.family.happiness.network.response

import com.family.happiness.room.user.User

data class SignInResponse(
    val user: User,
    val token: String,
    val familyId: String?,
)
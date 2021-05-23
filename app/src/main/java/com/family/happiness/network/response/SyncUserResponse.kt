package com.family.happiness.network.response

import com.family.happiness.room.user.User

data class SyncUserResponse(
    val users: List<User>
)
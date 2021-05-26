package com.family.happiness.network.request

import com.family.happiness.room.contributor.Contributor
import com.family.happiness.room.wish.Wish

data class SyncWishResponse(
    val wishes: List<Wish>,
    val contributors: List<Contributor>,
)
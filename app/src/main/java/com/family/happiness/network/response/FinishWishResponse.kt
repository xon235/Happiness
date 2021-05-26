package com.family.happiness.network.response

import com.family.happiness.room.contributor.Contributor
import com.family.happiness.room.wish.Wish

data class FinishWishResponse(
    val wish: Wish,
    val contributors: List<Contributor>
)
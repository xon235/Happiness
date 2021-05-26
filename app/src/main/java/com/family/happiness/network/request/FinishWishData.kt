package com.family.happiness.network.request

import com.family.happiness.room.contributor.Contributor

data class FinishWishData(
    val wishId: Int,
    val contributors: List<String>
)
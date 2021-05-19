package com.family.happiness.network.response

data class PersonalDataResponse(
    val token: String,
    val userId: String,
    val familyId: String?,
)
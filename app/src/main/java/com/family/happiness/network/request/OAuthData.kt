package com.family.happiness.network.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OAuthData(
    val type: String,
    val oAuthToken: String,
    val name: String,
    val photoUrl: String
) : Parcelable
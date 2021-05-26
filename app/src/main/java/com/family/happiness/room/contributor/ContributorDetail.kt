package com.family.happiness.room.contributor

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContributorDetail(
    @Embedded
    val contributor: Contributor,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: User
): Parcelable
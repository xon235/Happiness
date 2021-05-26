package com.family.happiness.room.wish

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.family.happiness.room.contributor.Contributor
import com.family.happiness.room.contributor.ContributorDetail
import com.family.happiness.room.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishDetail(
    @Embedded
    val wish: Wish,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: User,
    @Relation(
        entity = Contributor::class,
        parentColumn = "id",
        entityColumn = "wish_id"
    )
    val contributors: List<ContributorDetail>
): Parcelable
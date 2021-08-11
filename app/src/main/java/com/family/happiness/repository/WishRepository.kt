package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.DeleteWishData
import com.family.happiness.network.request.FinishWishData
import com.family.happiness.network.request.WriteWishData
import com.family.happiness.room.contributor.ContributorDao
import com.family.happiness.room.wish.WishDao
import kotlinx.coroutines.flow.distinctUntilChanged

class WishRepository(
    private val wishDao: WishDao,
    private val contributorDao: ContributorDao,
    private val happinessApi: HappinessApi,
) : BaseRepository() {

    val wishDetails = wishDao.getAllWishDetail().distinctUntilChanged()

    // Api
    suspend fun writeWish(
        writeWishData: WriteWishData
    ) = safeApiCall {
        val writeWishResponse = happinessApi.writeWish(writeWishData)
        wishDao.upsert(listOf(writeWishResponse.wish))
    }

    suspend fun finishWish(
        finishWishData: FinishWishData
    ) = safeApiCall {
        val finishWishResponse = happinessApi.finishWish(finishWishData)
        wishDao.update(listOf(finishWishResponse.wish))
        contributorDao.insert(finishWishResponse.contributors)
    }

    suspend fun deleteWish(
        deleteWishData: DeleteWishData
    ) = safeApiCall {
        happinessApi.deleteWish(deleteWishData)
        wishDao.deleteById(deleteWishData.wishId)
    }

    suspend fun syncWish() = safeApiCall {
        val getWishResponse = happinessApi.getWish()
        wishDao.sync(getWishResponse.wishes)
        contributorDao.sync(getWishResponse.contributors)
    }

    suspend fun deleteAllData() {
        wishDao.deleteNotIn(emptyList())
        contributorDao.deleteNotIn(emptyList())
    }
}
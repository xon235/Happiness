package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.DeleteWishData
import com.family.happiness.network.request.FinishWishData
import com.family.happiness.network.request.WriteMailData
import com.family.happiness.network.request.WriteWishData
import com.family.happiness.room.contributor.Contributor
import com.family.happiness.room.contributor.ContributorDao
import com.family.happiness.room.wish.Wish
import com.family.happiness.room.wish.WishDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishRepository(
    private val wishDao: WishDao,
    private val contributorDao: ContributorDao,
    private val happinessApi: HappinessApi,
) : BaseRepository() {

    val wishDetails = wishDao.getAll()

    suspend fun insertWish(wishes: List<Wish>) = withContext(Dispatchers.IO){
        wishDao.insert(wishes)
    }

    suspend fun deleteWishById(wishId: Int) = withContext(Dispatchers.IO){
        wishDao.deleteById(wishId)
    }

    suspend fun insertContributor(contributors: List<Contributor>) = withContext(Dispatchers.IO){
        contributorDao.insert(contributors)
    }

    suspend fun writeWish(
        writeWishData: WriteWishData
    ) = safeApiCall {
        happinessApi.writeWish(writeWishData)
    }

    suspend fun deleteWish(
        deleteWishData: DeleteWishData
    ) = safeApiCall {
        happinessApi.deleteWish(deleteWishData)
    }

    suspend fun finishWish(
        finishWishData: FinishWishData
    ) = safeApiCall {
        happinessApi.finishWish(finishWishData)
    }

    suspend fun syncWish() = safeApiCall {
        happinessApi.syncWish()
    }
}
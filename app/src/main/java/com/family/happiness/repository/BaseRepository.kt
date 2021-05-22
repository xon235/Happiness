package com.family.happiness.repository

import com.family.happiness.network.SafeResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): SafeResource<T> {
        return withContext(Dispatchers.IO){
            try {
                SafeResource.Success(apiCall.invoke())
            } catch (throwable: Throwable){
                SafeResource.Failure(throwable)
            }
        }
    }
}

package com.family.happiness.network

import com.family.happiness.network.request.GetSmsData
import com.family.happiness.network.response.JoinFamilyResponse
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.network.request.OAuthData
import com.family.happiness.network.request.SignUpData
import com.family.happiness.network.response.UploadPhotosResponse
import com.family.happiness.room.user.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

enum class HappinessApiStatus { LOADING, ERROR, DONE }

interface HappinessApi {

    @POST("signIn")
    suspend fun signIn(@Body oAuthData: OAuthData): PersonalDataResponse

    @POST("getSmsCode")
    suspend fun getSmsCode(@Body getSmsData: GetSmsData): ResponseBody

    @POST("signUp")
    suspend fun signUp(@Body signUpData: SignUpData): PersonalDataResponse

    @GET("family")
    suspend fun createFamily(): JoinFamilyResponse

    @POST("family")
    suspend fun joinFamily(@Body familyId: String): JoinFamilyResponse

    @DELETE("family")
    suspend fun leaveFamily()

    @Multipart
    @POST("upload/photo")
    suspend fun uploadPhotos(
        @Part("isNewEvent") isNewEvent: Boolean,
        @Part("eventName") eventName: String,
        @Part("tags") tags: List<User>,
        @Part parts: List<MultipartBody.Part>
    ): UploadPhotosResponse
}
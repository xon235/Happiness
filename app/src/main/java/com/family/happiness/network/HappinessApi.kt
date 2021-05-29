package com.family.happiness.network

import com.family.happiness.network.request.*
import com.family.happiness.network.response.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

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
    suspend fun joinFamily(@Body joinFamilyData: JoinFamilyData): JoinFamilyResponse

    @DELETE("family")
    suspend fun leaveFamily()

    @Multipart
    @POST("upload/photo")
    suspend fun uploadPhoto(
        @Part("isNewEvent") isNewEvent: Boolean,
        @Part("eventName") eventName: String,
        @Part("userIds") userIds: List<String>,
        @Part parts: List<MultipartBody.Part>
    ): UploadPhotosResponse

    @GET("sync/user")
    suspend fun syncUser(): SyncUserResponse

    @POST("mail")
    suspend fun writeMail(@Body writeMailData: WriteMailData)

    @POST("wish")
    suspend fun writeWish(@Body writeWishData: WriteWishData): WriteWishResponse

    @HTTP(method="DELETE", hasBody=true, path="wish")
    suspend fun deleteWish(@Body deleteWishData: DeleteWishData)

    @PUT("wish")
    suspend fun finishWish(@Body finishWishData: FinishWishData): FinishWishResponse

    @GET("wish")
    suspend fun getWish(): GetWishResponse
}
package com.family.happiness.network

import com.family.happiness.room.Image
import com.family.happiness.room.Member
import com.family.happiness.room.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

data class PhoneData(
        val phone: String
)

data class AuthData(
        val id: String,
        val session: String
)

data class TokenData(
        val type: String,
        val token: String
)

data class ProfileData(
        val name: String,
        val phone: String,
        val photoUrl: String
)

data class MemberData(
    val id: String,
    val profileData: ProfileData
)

fun MemberData.toMember(): Member{
    return Member(
            id,
            profileData.name,
            profileData.phone,
            profileData.photoUrl
    )
}

data class SignUpData(
        val tokenData: TokenData,
        val profileData: ProfileData,
        val smsCode: String
)

data class JoinFamilyData(
        val authData: AuthData,
        val family: String
)

data class BasicResponse (
        val result: Boolean,
        val message: String?
)

data class UserResponse (
        val result: Boolean,
        val message: String?,
        val user: User?
)

data class FamilyResponse (
        val result: Boolean,
        val message: String?,
        val family: String?,
        val members: List<MemberData>?
)

data class UploadImageResponse(
    val result: Boolean,
    val message: String?,
    val images: List<Image>?
)

private const val BASE_URL = "http://18.219.209.22:5000"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
val httpClient = OkHttpClient.Builder().addInterceptor(logging)

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
//    .client(httpClient.build())
    .build()

interface HappinessApi{
    @POST("requestSmsCode")
    suspend fun requestSmsCode(@Body body: PhoneData): BasicResponse

    @POST("signIn")
    suspend fun signIn(@Body body: AuthData): BasicResponse

    @POST("signInWithToken")
    suspend fun signInWithToken(@Body body: TokenData): UserResponse

    @POST("signUp")
    suspend fun signUp(@Body body: SignUpData): UserResponse

    @POST("createFamily")
    suspend fun createFamily(@Body body: AuthData): FamilyResponse

    @POST("joinFamily")
    suspend fun joinFamily(@Body body: JoinFamilyData): FamilyResponse

    @POST("getMembers")
    suspend fun getMembers(@Body body: AuthData): FamilyResponse

    @POST("leaveFamily")
    suspend fun leaveFamily(@Body body: AuthData): BasicResponse

    @Multipart
    @POST("uploadImages")
    suspend fun uploadImages(
            @Part("authData") authData: AuthData,
            @Part("isNewAlbum") isNewAlbum: Boolean,
            @Part("album") album: String,
            @Part("tagged") tagged: List<Member>,
            @Part files: List<MultipartBody.Part>
    ): UploadImageResponse
}

class HappinessApiException(message:String): Exception(message)

enum class HappinessApiStatus { LOADING, ERROR, DONE }


object HappinessApiService {
    val retrofitService: HappinessApi by lazy {
        retrofit.create(HappinessApi::class.java)
    }
}
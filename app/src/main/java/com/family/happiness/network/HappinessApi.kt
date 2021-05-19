package com.family.happiness.network

import com.family.happiness.network.response.JoinFamilyResponse
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.network.request.OAuthData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

//data class PhoneData(
//        val phone: String
//)
//
//data class AuthData(
//        val id: String,
//        val session: String
//)
//
//data class TokenData(
//        val type: String,
//        val token: String
//)
//
//data class ProfileData(
//        val name: String,
//        val phone: String,
//        val photoUrl: String
//)
//
//data class MemberData(
//    val id: String,
//    val profileData: ProfileData
//)
//
//fun MemberData.toMember(): Member{
//    return Member(
//            id,
//            profileData.name,
//            profileData.phone,
//            profileData.photoUrl
//    )
//}
//
//data class SignUpData(
//        val tokenData: TokenData,
//        val profileData: ProfileData,
//        val smsCode: String
//)
//
//data class JoinFamilyData(
//        val authData: AuthData,
//        val family: String
//)
//
//data class BasicResponse (
//        val result: Boolean,
//        val message: String?
//)
//
//data class UserResponse (
//        val result: Boolean,
//        val message: String?,
//        val user: User?
//)
//
//data class FamilyResponse (
//        val result: Boolean,
//        val message: String?,
//        val family: String?,
//        val members: List<MemberData>?
//)
//
//data class UploadImageResponse(
//    val result: Boolean,
//    val message: String?,
//    val images: List<Image>?
//)

interface HappinessApi {

//    @POST("signIn")
//    suspend fun signIn(@Body body: AuthData): SignInRe

    @POST("family")
    suspend fun joinFamily(@Body familyId: String): JoinFamilyResponse

    @DELETE("family")
    suspend fun leaveFamily()

    @POST("signIn")
    suspend fun signIn(@Body oAuthData: OAuthData): PersonalDataResponse

//    @POST("requestSmsCode")
//    suspend fun requestSmsCode(@Body body: PhoneData): BasicResponse
//
//    @POST("signIn")
//    suspend fun signIn(@Body body: AuthData): BasicResponse
//
//    @POST("signInWithToken")
//    suspend fun signInWithToken(@Body body: TokenData): UserResponse
//
//    @POST("signUp")
//    suspend fun signUp(@Body body: SignUpData): UserResponse
//
//    @POST("createFamily")
//    suspend fun createFamily(@Body body: AuthData): FamilyResponse
//
//    @POST("joinFamily")
//    suspend fun joinFamily(@Body body: JoinFamilyData): FamilyResponse
//
//    @POST("getMembers")
//    suspend fun getMembers(@Body body: AuthData): FamilyResponse
//
//    @POST("leaveFamily")
//    suspend fun leaveFamily(@Body body: AuthData): BasicResponse
//
//    @Multipart
//    @POST("uploadImages")
//    suspend fun uploadImages(
//            @Part("authData") authData: AuthData,
//            @Part("isNewAlbum") isNewAlbum: Boolean,
//            @Part("album") album: String,
//            @Part("tagged") tagged: List<Member>,
//            @Part files: List<MultipartBody.Part>
//    ): UploadImageResponse
}

enum class HappinessApiStatus { LOADING, ERROR, DONE }

object HappinessApiService {
    private const val BASE_URL = "http://18.219.209.22:5000"

    val api: HappinessApi by lazy {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
            )
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()

        retrofit.create(HappinessApi::class.java)
    }
}
package com.family.happiness.repository

import com.family.happiness.network.*
import com.family.happiness.room.*
import com.family.happiness.room.contributor.ContributorDao
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.PhotoDao
import com.family.happiness.room.tag.TagDao
import com.family.happiness.room.user.User
import com.family.happiness.room.user.UserDao
import com.family.happiness.room.wish.Wish
import com.family.happiness.room.wish.WishDao
import kotlinx.coroutines.*
import okhttp3.MultipartBody

class HappinessRepository(
    private val userDao: UserDao,
    private val eventDao: EventDao,
    private val photoDao: PhotoDao,
    private val tagDao: TagDao,
    private val wishDao: WishDao,
    private val contributorDao: ContributorDao,
    private val happinessApi: HappinessApi
) {

//    val user = userDAO.get()
//    val members = memberDAO.getAll()
//    val images = imageDAO.getAll()
//    val albums = imageDAO.getAlbums()
//    val wishes = wishDAO.getAll()
//
//    suspend fun signIn(authData: AuthData) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.signIn(authData)
//            if (!response.result) {
//                clearUserData()
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun signInWithToken(tokenData: TokenData) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.signInWithToken(tokenData)
//            if (response.result) {
//                newUser(response.user!!)
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun newUser(user: User) {
//        withContext(Dispatchers.IO) {
//            userDAO.replace(user)
//        }
//    }
//
//    suspend fun clearUserData() {
//        withContext(Dispatchers.IO) {
//            userDAO.deleteAll()
////            memberDAO.deleteAll()
//        }
//    }
//
//    suspend fun updateSessionKey(SessionKey: String) {
//        withContext(Dispatchers.IO) {
//            userDAO.updateSessionKey(SessionKey)
//        }
//    }
//
//    suspend fun updateNameAndPhone(name: String, phone: String) {
//        withContext(Dispatchers.IO) {
//            userDAO.updateNameAndPhone(name, phone)
//        }
//    }
//
//    suspend fun updateUserImageUrl(imageUrl: String) {
//        withContext(Dispatchers.IO) {
//            userDAO.updateUserImageUrl(imageUrl)
//        }
//    }
//
//    suspend fun updateMembers(authData: AuthData) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.getMembers(authData)
//            if (response.result) {
//                memberDAO.replaceAll(response.members!!.map { memberData -> memberData.toMember() })
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun createFamily(authData: AuthData) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.createFamily(authData)
//            if (response.result) {
//                userDAO.updateUserFamily(response.family!!)
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun joinFamily(authData: AuthData, family: String) {
//        withContext(Dispatchers.IO) {
//            val response =
//                HappinessApiService.retrofitService.joinFamily(JoinFamilyData(authData, family))
//            if (response.result) {
//                userDAO.updateUserFamily(response.family!!)
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun leaveFamily(authData: AuthData) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.leaveFamily(authData)
//            if (response.result) {
//                userDAO.updateUserFamily(null)
//                imageDAO.deleteAll()
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun insertImages(images: List<Image>) {
//        withContext(Dispatchers.IO) {
//            imageDAO.insert(images)
//        }
//    }
//
//    suspend fun deleteImage(image: Image) {
//        withContext(Dispatchers.IO) {
//            imageDAO.delete(image)
//        }
//    }
//
//    suspend fun moveImage(image: Image, album: String) {
//        withContext(Dispatchers.IO) {
//
//            imageDAO.update(
//                Image(
//                    image.url,
//                    image.id_user,
//                    album,
//                    image.timestamp
//                )
//            )
//        }
//    }
//
//    suspend fun insertMembers(members: List<Member>) {
//        withContext(Dispatchers.IO) {
//            memberDAO.insert(members)
//        }
//    }
//
//    suspend fun uploadImage(
//        isNewAlbum: Boolean,
//        album: String,
//        tagged: List<Member>,
//        files: List<MultipartBody.Part>
//    ) {
//        withContext(Dispatchers.IO) {
//            val response = HappinessApiService.retrofitService.uploadImages(
//                userDAO.getSync().toAuthData(),
//                isNewAlbum,
//                album,
//                tagged,
//                files
//            )
//            if (response.result) {
//                response.images?.let { insertImages(it) }
//            } else {
//                throw HappinessApiException(response.message!!)
//            }
//        }
//    }
//
//    suspend fun insertWishes(wishes: List<Wish>) {
//        withContext(Dispatchers.IO) {
//            wishDAO.insert(wishes)
//        }
//    }
}
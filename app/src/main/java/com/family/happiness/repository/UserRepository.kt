package com.family.happiness.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.family.happiness.PreferenceKeys
import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.*
import com.family.happiness.room.user.UserDao
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class UserRepository(
    private val personalDataDatastore: DataStore<androidx.datastore.preferences.core.Preferences>,
    private val userDao: UserDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

    val users = userDao.getAll()

    val personalDataPreferencesFlow = personalDataDatastore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception, "Error reading preferences.")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            PersonalDataPreferences(
                it[PreferenceKeys.TOKEN],
                it[PreferenceKeys.USER_ID],
                it[PreferenceKeys.FAMILY_ID]
            )
        }

    val me = combine(personalDataPreferencesFlow, users) { personalDataPreferences, users ->
        users.find { it.id == personalDataPreferences.userId }
    }

    val members = combine(personalDataPreferencesFlow, users) { personalDataPreferences, users ->
        users.filter { it.id != personalDataPreferences.userId }
    }

    // Api
    suspend fun signIn(oAuthData: OAuthData) = safeApiCall {
        val signInData = SignInData(
            personalDataDatastore.data.map { it[PreferenceKeys.FCM_TOKEN] }.first()!!,
            oAuthData
        )
        val signInResponse = happinessApi.signIn(signInData)
        personalDataDatastore.edit {
            it[PreferenceKeys.TOKEN] = signInResponse.token
            it[PreferenceKeys.USER_ID] = signInResponse.user.id
            if (signInResponse.familyId == null) {
                it.remove(PreferenceKeys.FAMILY_ID)
            } else {
                it[PreferenceKeys.FAMILY_ID] = signInResponse.familyId
            }
        }
        userDao.insert(listOf(signInResponse.user))
    }

    suspend fun signUp(signUpData: SignUpData) = safeApiCall {
        happinessApi.signUp(signUpData)
    }

    suspend fun getSmsCode(getSmsData: GetSmsData) = safeApiCall {
        happinessApi.getSmsCode(getSmsData)
    }

    suspend fun createFamily() = safeApiCall {
        val joinFamilyResponse = happinessApi.createFamily()
        personalDataDatastore.edit {
            it[PreferenceKeys.FAMILY_ID] = joinFamilyResponse.familyId
        }
    }

    suspend fun joinFamily(familyId: String) = safeApiCall {
        val joinFamilyResponse = happinessApi.joinFamily(JoinFamilyData(familyId))
        personalDataDatastore.edit {
            it[PreferenceKeys.FAMILY_ID] = joinFamilyResponse.familyId
        }
    }

    suspend fun leaveFamily() = safeApiCall {
        happinessApi.leaveFamily()
        personalDataDatastore.edit {
            it.remove(PreferenceKeys.FAMILY_ID)
        }
    }

    suspend fun syncUser() = safeApiCall {
        val syncUserResponse = happinessApi.syncUser()
        userDao.sync(syncUserResponse.users)
    }

    // Datastore
    suspend fun deleteAllData() {
        personalDataDatastore.edit {
            it.remove(PreferenceKeys.TOKEN)
            it.remove(PreferenceKeys.USER_ID)
            it.remove(PreferenceKeys.FAMILY_ID)
        }
        userDao.deleteNotIn(emptyList())
    }

    suspend fun insertFcmToken(fcmToken: String) {
        personalDataDatastore.edit {
            it[PreferenceKeys.FCM_TOKEN] = fcmToken
        }
    }

    data class PersonalDataPreferences(
        val token: String?,
        val userId: String?,
        val familyId: String?
    )
}
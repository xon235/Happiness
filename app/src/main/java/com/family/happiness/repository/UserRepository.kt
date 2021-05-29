package com.family.happiness.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.family.happiness.PreferenceKeys
import com.family.happiness.network.HappinessApi
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.GetSmsData
import com.family.happiness.network.request.JoinFamilyData
import com.family.happiness.network.request.OAuthData
import com.family.happiness.network.request.SignUpData
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.room.user.UserDao
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException

class UserRepository(
    private val personalDataDatastore: DataStore<androidx.datastore.preferences.core.Preferences>,
    private val userDao: UserDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

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

    val users = userDao.getAll()

    val members = combine(
        personalDataPreferencesFlow,
        users
    ) {
            personalDataPreferences, users  ->
        users.filter {
            it.id != personalDataPreferences.userId
        }
    }

    val me = combine(
        personalDataPreferencesFlow,
        users
    ) {
            personalDataPreferences, users  ->
        users.find {
            it.id == personalDataPreferences.userId
        }
    }

    suspend fun signIn(oAuthData: OAuthData): SafeResource<PersonalDataResponse> {
        return safeApiCall { happinessApi.signIn(oAuthData) }
    }

    suspend fun signUp(signUpData: SignUpData): SafeResource<PersonalDataResponse> {
        return safeApiCall { happinessApi.signUp(signUpData) }
    }

    suspend fun getSmsCode(getSmsData: GetSmsData): SafeResource<ResponseBody>{
        return safeApiCall { happinessApi.getSmsCode(getSmsData) }
    }

    suspend fun insertPersonalData(personalDataResponse: PersonalDataResponse) {
        personalDataDatastore.edit {
            it[PreferenceKeys.TOKEN] = personalDataResponse.token
            it[PreferenceKeys.USER_ID] = personalDataResponse.userId
            if(personalDataResponse.familyId == null){
                it.remove(PreferenceKeys.FAMILY_ID)
            } else {
                it[PreferenceKeys.FAMILY_ID] = personalDataResponse.familyId
            }
        }
    }

    suspend fun insertFamilyId(familyId: String) {
        personalDataDatastore.edit {
            it[PreferenceKeys.FAMILY_ID] = familyId
        }
    }

    suspend fun deleteFamilyId() {
        personalDataDatastore.edit {
            it.remove(PreferenceKeys.FAMILY_ID)
        }
    }

    suspend fun deleteAllPersonalData() {
        personalDataDatastore.edit {
            it.remove(PreferenceKeys.TOKEN)
            it.remove(PreferenceKeys.USER_ID)
            it.remove(PreferenceKeys.FAMILY_ID)
        }
    }

    suspend fun createFamily() = safeApiCall {
        happinessApi.createFamily()
    }

    suspend fun joinFamily(familyId: String) = safeApiCall {
        happinessApi.joinFamily(JoinFamilyData(familyId))
    }

    suspend fun leaveFamily() = safeApiCall {
        happinessApi.leaveFamily()
    }

    suspend fun syncUser() = safeApiCall {
        userDao.sync(happinessApi.syncUser().users)
    }

    data class PersonalDataPreferences(
        val token: String?,
        val userId: String?,
        val familyId: String?
    )
}
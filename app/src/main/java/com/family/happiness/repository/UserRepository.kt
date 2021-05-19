package com.family.happiness.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.family.happiness.network.HappinessApiService
import com.family.happiness.network.SafeResource
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.room.user.UserDao
import com.family.happiness.network.request.OAuthData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class UserRepository(
    private val personalDataDatastore: DataStore<androidx.datastore.preferences.core.Preferences>,
    private val userDao: UserDao,
) : BaseRepository() {
    private object PreferenceKeys {
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("user_id")
        val FAMILY_ID = stringPreferencesKey("family_id")
    }

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

    val me = combine(
        personalDataPreferencesFlow,
        users
    ) {
            personalDataPreferences, users  ->
        users.find {
            it.id == personalDataPreferences.userId
        }
    }

    val members = combine(
        personalDataPreferencesFlow,
        users
    ) {
            personalDataPreferences, users  ->
        users.filter {
            it.id != personalDataPreferences.userId
        }
    }

    suspend fun joinFamily(familyId: String) = safeApiCall {
        HappinessApiService.api.joinFamily(familyId)
    }

    suspend fun leaveFamily() = safeApiCall {
        HappinessApiService.api.leaveFamily()
    }

    suspend fun signIn(oAuthData: OAuthData): SafeResource<PersonalDataResponse> {
        return safeApiCall { HappinessApiService.api.signIn(oAuthData) }
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

    suspend fun deleteAllPersonalData() {
        personalDataDatastore.edit {
            it.remove(PreferenceKeys.TOKEN)
            it.remove(PreferenceKeys.USER_ID)
            it.remove(PreferenceKeys.FAMILY_ID)
        }
    }

    data class PersonalDataPreferences(
        val token: String?,
        val userId: String?,
        val familyId: String?
    )
}
package com.family.happiness

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.family.happiness.network.HappinessApi
import com.family.happiness.repository.*
import com.family.happiness.room.HappinessRoomDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object PreferenceKeys {
    val TOKEN = stringPreferencesKey("token")
    val USER_ID = stringPreferencesKey("user_id")
    val FAMILY_ID = stringPreferencesKey("family_id")
    val FCM_TOKEN = stringPreferencesKey("fcm_token")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "personal_data")

class HappinessApplication : Application() {
    private val database: HappinessRoomDatabase by lazy {
        HappinessRoomDatabase.getDatabase(this)
    }

    private val happinessApi: HappinessApi by lazy {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().apply {
                        runBlocking {
                            dataStore.data.map{ it[PreferenceKeys.TOKEN] }.first()?.let {
                                addHeader("Authorization", "Bearer $it")
                            }
                            dataStore.data.map{ it[PreferenceKeys.FCM_TOKEN] }.first()?.let {
                                addHeader("FCM-Token", it)
                            }
                        }
                    }.build()
                )
            }
            .addInterceptor(
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(getString(R.string.server_url))
            .client(httpClient)
            .build()

        retrofit.create(HappinessApi::class.java)
    }

    val userRepository by lazy {
        UserRepository(
            (this as Context).dataStore,
            database.userDao(),
            happinessApi
        )
    }

    val mailRepository by lazy {
        MailRepository(
            database.mailDao(),
            happinessApi
        )
    }

    val albumRepository by lazy {
        AlbumRepository(
            database.eventDao(),
            database.tagDao(),
            database.photoDao(),
            happinessApi
        )
    }

    val wishRepository by lazy {
        WishRepository(
            database.wishDao(),
            database.contributorDao(),
            happinessApi
        )
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            Timber.plant(Timber.DebugTree())
        }
    }
}
package com.family.happiness

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.repository.UserRepository
import com.family.happiness.room.HappinessRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "personal_data")

class HappinessApplication : Application() {
    val database by lazy {
        HappinessRoomDatabase.getDatabase(this)
    }
    val happinessRepository by lazy {
        HappinessRepository(
            database.userDao(),
            database.eventDao(),
            database.photoDao(),
            database.tagDao(),
            database.wishDao(),
            database.contributorDao(),
        )
    }
    val userRepository by lazy {
        UserRepository(
            (this as Context).dataStore,
            database.userDao()
        )
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            Timber.plant(Timber.DebugTree())
        }
    }
}
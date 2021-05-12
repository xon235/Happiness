package com.family.happiness

import android.app.Application
import com.family.happiness.rooms.HappinessRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HappinessApplication: Application() {
    val database by lazy { HappinessRoomDatabase.getDatabase(this) }
    val repository by lazy { HappinessRepository(database.userDAO(), database.memberDAO(), database.imageDAO(), database.wishDAO()) }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            Timber.plant(Timber.DebugTree())
        }
    }
}
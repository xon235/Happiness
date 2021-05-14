package com.family.happiness.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [User::class, Member::class, Image::class, Wish::class], version = 1, exportSchema = false)
abstract class HappinessRoomDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun memberDAO(): MemberDAO
    abstract fun imageDAO(): ImageDAO
    abstract fun wishDAO(): WishDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: HappinessRoomDatabase? = null

        fun getDatabase(
            context: Context,
        ): HappinessRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappinessRoomDatabase::class.java,
                    "happiness_database"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }
}
package com.family.happiness.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.family.happiness.room.contributor.Contributor
import com.family.happiness.room.contributor.ContributorDao
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.mail.Mail
import com.family.happiness.room.mail.MailDao
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDao
import com.family.happiness.room.tag.Tag
import com.family.happiness.room.tag.TagDao
import com.family.happiness.room.user.User
import com.family.happiness.room.user.UserDao
import com.family.happiness.room.wish.Wish
import com.family.happiness.room.wish.WishDao

@Database(
    entities = [
        User::class,
        Mail::class,
        Event::class,
        Photo::class,
        Tag::class,
        Wish::class,
        Contributor::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class HappinessRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mailDao(): MailDao
    abstract fun eventDao(): EventDao
    abstract fun photoDao(): PhotoDao
    abstract fun tagDao(): TagDao
    abstract fun wishDao(): WishDao
    abstract fun contributorDao(): ContributorDao

    companion object {
        @Volatile
        private var INSTANCE: HappinessRoomDatabase? = null

        fun getDatabase(
            context: Context,
        ): HappinessRoomDatabase {
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
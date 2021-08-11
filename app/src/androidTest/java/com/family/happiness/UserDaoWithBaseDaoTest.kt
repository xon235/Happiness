package com.family.happiness

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.family.happiness.room.HappinessRoomDatabase
import com.family.happiness.room.user.User
import com.family.happiness.room.user.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoWithBaseDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: HappinessRoomDatabase
    private lateinit var newUsers: MutableList<User>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HappinessRoomDatabase::class.java).build()
        userDao = db.userDao()
        newUsers = MutableList(10) {
            User("id$it", "name$it", "phone$it", "photoUrl$it")
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun selectAll() {
        runBlocking {
            userDao.insert(newUsers)
            assertThat(userDao.selectAll().first(), equalTo(newUsers))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        runBlocking {
            userDao.insert(newUsers)
            userDao.deleteAll()
            assertThat(userDao.selectAll().first(), equalTo(emptyList()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun upsert() {
        runBlocking {
            userDao.insert(newUsers.subList(0, 5))
            newUsers[3] = newUsers[3].copy(phone = newUsers[3].phone + "+")
            newUsers[4] = newUsers[4].copy(photoUrl = newUsers[4].photoUrl + "+")
            userDao.upsert(newUsers.subList(3, 8))
            assertThat(userDao.selectAll().first(), equalTo(newUsers.subList(0, 8)))
        }
    }

    @Test
    @Throws(Exception::class)
    fun sync() {
        runBlocking {
            userDao.insert(newUsers.subList(0, 5))
            newUsers[3] = newUsers[3].copy(phone = newUsers[3].phone + "+")
            newUsers[4] = newUsers[4].copy(photoUrl = newUsers[4].photoUrl + "+")
            userDao.sync(newUsers.subList(3, 8))
            assertThat(userDao.selectAll().first(), equalTo(newUsers.subList(3, 8)))
        }
    }
}
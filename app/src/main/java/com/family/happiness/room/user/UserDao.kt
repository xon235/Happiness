package com.family.happiness.room.user

import androidx.room.Dao
import androidx.room.Query
import com.family.happiness.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao: BaseDao<User>("user") {

    @Query("SELECT * FROM user")
    abstract fun selectAll(): Flow<List<User>>

    fun getAll() = selectAll()

    suspend fun deleteNotIn(users: List<User>) = deleteAll()
}
package com.family.happiness.room.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(users: List<User>)

    @Transaction
    suspend fun refresh(users: List<User>){
        deleteAll()
        insert(users)
    }
}
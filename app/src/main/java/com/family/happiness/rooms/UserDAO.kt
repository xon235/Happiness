package com.family.happiness.rooms

import androidx.room.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

@Dao
interface  UserDAO {
    @Query("SELECT * FROM user LIMIT 1")
    fun get(): Flow<User>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getSync(): User

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("UPDATE user SET session = :sessionKey")
    suspend fun updateSessionKey(sessionKey: String)

    @Query("UPDATE user SET name = :name, phone = :phone")
    suspend fun updateNameAndPhone(name: String, phone: String)

    @Query("UPDATE user SET photo_url = :imageUrl")
    suspend fun updateUserImageUrl(imageUrl: String)

    @Query("UPDATE user SET id_family = :family")
    suspend fun updateUserFamily(family: String?)

    @Transaction
    suspend fun replace(user: User){
        deleteAll()
        insert(user)
    }
}
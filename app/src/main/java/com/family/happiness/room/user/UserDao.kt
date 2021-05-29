package com.family.happiness.room.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("DELETE FROM user WHERE id NOT IN (:userIds)")
    suspend fun deleteNotIn(userIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: List<User>): List<Long>


    // TODO make base Dao
    @Update
    suspend fun update(users: List<User>)

    @Transaction
    suspend fun upsert(users: List<User>) {
        val insertResult = insert(users)
        val updateList: MutableList<User> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(users[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(users: List<User>) {
        deleteNotIn(users.map { it.id })
        upsert(users)
    }
}
package com.family.happiness.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WishDAO {

    @Query("SELECT * FROM wish ORDER BY timestamp_create DESC")
    fun getAll(): Flow<List<Wish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishes: List<Wish>)

    @Query("DELETE FROM wish")
    suspend fun deleteAll()
}
package com.family.happiness.room.wish

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishes: List<Wish>)

    @Query("DELETE FROM wish WHERE id = :wishId")
    suspend fun deleteById(wishId: Int)

    @Query("SELECT * FROM wish")
    fun getAll(): Flow<List<WishDetail>>
}
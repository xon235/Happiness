package com.family.happiness.room.wish

import androidx.room.*
import com.family.happiness.room.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface WishDao {

    @Query("SELECT * FROM wish ORDER BY CASE WHEN timestamp_close IS NULL THEN 1 ELSE 0 END DESC, timestamp_open DESC, timestamp_close DESC")
    fun getAll(): Flow<List<WishDetail>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wishes: List<Wish>): List<Long>

    @Query("DELETE FROM wish WHERE id = :wishId")
    suspend fun deleteById(wishId: Int)

    @Query("DELETE FROM wish WHERE id NOT IN (:wishIds)")
    suspend fun deleteNotIn(wishIds: List<Int>)

    @Update
    suspend fun update(wishes: List<Wish>)

    @Transaction
    suspend fun upsert(wishes: List<Wish>) {
        val insertResult = insert(wishes)
        val updateList: MutableList<Wish> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(wishes[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(wishes: List<Wish>) {
        deleteNotIn(wishes.map { it.id })
        upsert(wishes)
    }
}
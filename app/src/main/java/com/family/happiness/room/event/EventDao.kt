package com.family.happiness.room.event

import androidx.room.*
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.wish.Wish
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getAll(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(events: List<Event>): List<Long>

    @Query("DELETE FROM event WHERE id IN (:eventIds)")
    suspend fun deleteById(eventIds: List<Int>)

    @Query("DELETE FROM event WHERE id NOT IN (:eventIds)")
    suspend fun deleteNotIn(eventIds: List<Int>)

    @Update
    suspend fun update(events: List<Event>)

    @Transaction
    suspend fun upsert(events: List<Event>) {
        val insertResult = insert(events)
        val updateList: MutableList<Event> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(events[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(events: List<Event>) {
        deleteNotIn(events.map { it.id })
        upsert(events)
    }
}
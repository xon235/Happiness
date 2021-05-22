package com.family.happiness.room.photo

import androidx.room.Dao
import androidx.room.Query
import com.family.happiness.room.event.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
    fun getAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE event_id = :event_id")
    fun getByEvent(event_id: Int): Flow<List<Photo>>
}
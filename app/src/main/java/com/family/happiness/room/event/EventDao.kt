package com.family.happiness.room.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(events: List<Event>)

    @Query("SELECT * FROM event")
    fun getAll(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEventByPhoto(eventId: Int): Flow<Event>
}
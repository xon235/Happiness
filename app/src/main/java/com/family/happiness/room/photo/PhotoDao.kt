package com.family.happiness.room.photo

import androidx.room.*
import com.family.happiness.room.event.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photos: List<Photo>)

    @Query("SELECT * FROM photo")
    fun getAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE url = :url")
    fun getByUrl(url: String): Flow<Photo>

    @Query("SELECT * FROM photo WHERE event_id = :eventId")
    fun getByEvent(eventId: Int): Flow<List<Photo>>

    @Query("UPDATE photo SET event_id = :eventId WHERE url = :url")
    suspend fun changePhotoEvent(url: String, eventId: Int)

    @Delete
    suspend fun delete(photo: Photo)
}
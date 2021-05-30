package com.family.happiness.room.photo

import androidx.room.*
import com.family.happiness.room.event.Event
import com.family.happiness.room.wish.Wish
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
    fun getAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE url = :url")
    fun getPhotoDetailByUrl(url: String): Flow<PhotoDetail>

    @Query("SELECT * FROM photo WHERE event_id = :eventId")
    fun getPhotoByEvent(eventId: Int): Flow<List<Photo>>

    @Query("UPDATE photo SET event_id = :eventId WHERE url = :url")
    suspend fun changePhotoEvent(url: String, eventId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photos: List<Photo>): List<Long>

    @Delete
    suspend fun delete(photo: Photo)

    @Query("DELETE FROM photo WHERE url NOT IN (:photoUrls)")
    suspend fun deleteNotIn(photoUrls: List<String>)

    @Update
    suspend fun update(photos: List<Photo>)

    @Transaction
    suspend fun upsert(photos: List<Photo>) {
        val insertResult = insert(photos)
        val updateList: MutableList<Photo> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(photos[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(photos: List<Photo>) {
        deleteNotIn(photos.map { it.url })
        upsert(photos)
    }
}
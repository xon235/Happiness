package com.family.happiness.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDAO {

    @Query("SELECT * FROM image ORDER BY timestamp ASC")
    fun getAll(): Flow<List<Image>>

    @Query("SELECT DISTINCT album FROM image ORDER BY album ASC")
    fun getAlbums(): Flow<List<String>>

    @Query("SELECT * FROM image WHERE album=:album ORDER BY timestamp ASC")
    fun getImagesByName(album: String): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<Image>)

    @Query("DELETE FROM image")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(image: Image)

    @Update
    suspend fun update(image: Image)
}
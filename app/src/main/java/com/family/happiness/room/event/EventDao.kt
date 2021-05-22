package com.family.happiness.room.event

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getAll(): Flow<List<Event>>
}
package com.family.happiness.room.mail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MailDao {

    @Insert
    suspend fun insert(mail: Mail)

    @Query("SELECT * FROM mail ORDER BY time_sent DESC")
    fun getAll(): Flow<List<Mail>>
}
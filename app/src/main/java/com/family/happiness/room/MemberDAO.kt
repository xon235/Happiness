package com.family.happiness.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDAO {

    @Query("SELECT * FROM member ORDER BY name ASC")
    fun getAll(): Flow<List<Member>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(members: List<Member>)

    @Query("DELETE FROM member")
    fun deleteAll()

    @Transaction
    suspend fun replaceAll(members: List<Member>){
        deleteAll()
        insert(members)
    }
}
package com.family.happiness.room.mail

import androidx.room.*
import com.family.happiness.room.wish.Wish
import kotlinx.coroutines.flow.Flow

@Dao
interface MailDao {

    @Insert
    suspend fun insert(mails: List<Mail>): List<Long>

    @Query("SELECT * FROM mail ORDER BY time_sent DESC")
    fun getAll(): Flow<List<Mail>>

    @Query("DELETE FROM wish WHERE id = :wishId")
    suspend fun deleteById(wishId: Int)

    @Query("DELETE FROM mail WHERE id NOT IN (:mailIds)")
    suspend fun deleteNotIn(mailIds: List<Int>)

    @Update
    suspend fun update(mails: List<Mail>)

    @Transaction
    suspend fun upsert(mails: List<Mail>) {
        val insertResult = insert(mails)
        val updateList: MutableList<Mail> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(mails[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(mails: List<Mail>) {
        deleteNotIn(mails.map { it.id })
        upsert(mails)
    }
}
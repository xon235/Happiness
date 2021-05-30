package com.family.happiness.room.tag

import androidx.room.*
import com.family.happiness.room.photo.Photo

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tags: List<Tag>): List<Long>

    @Query("DELETE FROM tag WHERE id NOT IN (:tagIds)")
    suspend fun deleteNotIn(tagIds: List<Int>)

    @Update
    suspend fun update(tags: List<Tag>)

    @Transaction
    suspend fun upsert(tags: List<Tag>) {
        val insertResult = insert(tags)
        val updateList: MutableList<Tag> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(tags[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(tags: List<Tag>) {
        deleteNotIn(tags.map { it.id })
        upsert(tags)
    }
}
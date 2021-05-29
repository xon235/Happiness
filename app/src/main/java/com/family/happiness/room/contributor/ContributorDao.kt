package com.family.happiness.room.contributor

import androidx.room.*
import com.family.happiness.room.wish.Wish
import com.family.happiness.room.wish.WishDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface ContributorDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wishes: List<Contributor>): List<Long>

    @Query("DELETE FROM contributor WHERE id = :ContributorId")
    suspend fun deleteById(ContributorId: Int)

    @Query("DELETE FROM Contributor WHERE id NOT IN (:ContributorIds)")
    suspend fun deleteNotIn(ContributorIds: List<Int>)

    @Update
    suspend fun update(contributors: List<Contributor>)

    @Transaction
    suspend fun upsert(contributors: List<Contributor>) {
        val insertResult = insert(contributors)
        val updateList: MutableList<Contributor> = ArrayList()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(contributors[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }

    }

    @Transaction
    suspend fun sync(contributors: List<Contributor>) {
        deleteNotIn(contributors.map { it.id })
        upsert(contributors)
    }
}
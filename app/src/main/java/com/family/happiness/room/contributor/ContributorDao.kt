package com.family.happiness.room.contributor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface ContributorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contributors: List<Contributor>)
}
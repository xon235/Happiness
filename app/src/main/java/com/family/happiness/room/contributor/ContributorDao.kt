package com.family.happiness.room.contributor

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ContributorDao {

    @Insert
    suspend fun insert(contributors: List<Contributor>)
}
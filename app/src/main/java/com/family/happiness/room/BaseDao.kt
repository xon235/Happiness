package com.family.happiness.room

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T : Any>(val tableName: String) {

    @RawQuery
    protected abstract suspend fun runSelectQuery(rawQuery: SupportSQLiteQuery): List<T>

    private suspend fun selectAllSync() = runSelectQuery(
        SimpleSQLiteQuery("SELECT * FROM $tableName")
    )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entities: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Update
    abstract suspend fun update(entities: List<T>): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @Delete
    abstract suspend fun delete(entities: List<T>): Int

    @RawQuery
    protected abstract suspend fun runDeleteQuery(rawQuery: SupportSQLiteQuery): Int

    suspend fun deleteAll() = runDeleteQuery(
        SimpleSQLiteQuery("DELETE FROM $tableName")
    )

    suspend fun upsert(entities: List<T>) {
        val newEntities = entities.toSet()
        val oldEntities = selectAllSync().toSet()
        upsert(newEntities, oldEntities)
    }

    @Transaction
    open suspend fun upsert(newEntities: Set<T>, oldEntities: Set<T>) {
        val entitiesNotInOld = newEntities subtract oldEntities
        insert(entitiesNotInOld.toList())

        val entitiesInBoth = newEntities intersect oldEntities
        update(entitiesInBoth.toList())
    }

    @Transaction
    open suspend fun sync(entities: List<T>) {
        val newEntities = entities.toSet()
        val oldEntities = selectAllSync().toSet()

        val entitiesNotInNew = oldEntities subtract newEntities
        delete(entitiesNotInNew.toList())

        upsert(newEntities, oldEntities)
    }
}
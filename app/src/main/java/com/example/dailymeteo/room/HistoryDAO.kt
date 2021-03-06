package com.example.dailymeteo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDAO {

    @Query("SELECT * FROM HistoryEntity")
    fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE cityName = :name")
    fun getByName(name: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity")
    fun deleteAll()
}

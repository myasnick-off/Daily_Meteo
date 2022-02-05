package com.example.dailymeteo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var date: Long,
    val cityName: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

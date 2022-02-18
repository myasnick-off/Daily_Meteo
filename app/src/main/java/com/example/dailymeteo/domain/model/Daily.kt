package com.example.dailymeteo.domain.model

import com.example.dailymeteo.utils.WindDir

data class Daily(
    val dayMonth: String,
    val weekDay: String,
    val sunrise: String,
    val sunset: String,
    val temp: DailyTemp,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDir: WindDir,
    val cloudiness: Int,
    val uvIndex: Float,
    val precProb: Int,
    val description: String,
    val icon: String
)

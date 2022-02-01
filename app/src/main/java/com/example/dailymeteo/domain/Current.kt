package com.example.dailymeteo.domain

import com.example.dailymeteo.utils.WindDir


data class Current(
    val sunrise: String,
    val sunset: String,
    val temp: Int,
    val feelsLike: Int,
    val pressure: Int,
    val humidity: Int,
    val uvIndex: Float,
    val cloudiness: Int,
    val windSpeed: Double,
    val windDir: WindDir,
    val description: String,
    val icon: String
)
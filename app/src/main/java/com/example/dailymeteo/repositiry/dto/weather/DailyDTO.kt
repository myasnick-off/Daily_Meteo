package com.example.dailymeteo.repositiry.dto.weather

import com.google.gson.annotations.SerializedName

data class DailyDTO(
    @SerializedName("dt")
    val time: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempDTO,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDir: Int,
    @SerializedName("clouds")
    val cloudiness: Int,
    @SerializedName("uvi")
    val uvIndex: Float,
    @SerializedName("pop")
    val precProb: Float,
    val weather: List<WeatherDTO>
)

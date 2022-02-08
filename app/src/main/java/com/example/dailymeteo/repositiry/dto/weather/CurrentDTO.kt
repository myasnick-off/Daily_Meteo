package com.example.dailymeteo.repositiry.dto.weather

import com.google.gson.annotations.SerializedName

data class CurrentDTO(
    @SerializedName("dt")
    val time: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("uvi")
    val uvIndex: Float,
    val visibility: Int,
    @SerializedName("clouds")
    val cloudiness: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_deg")
    val windDir: Int,
    val weather: List<WeatherDTO>
)
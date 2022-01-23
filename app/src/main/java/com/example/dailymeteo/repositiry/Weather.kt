package com.example.dailymeteo.repositiry

data class Weather(
    val city: City = getDefaultCity(),
    val temp: Int = -100,
    val feelsLike: Int = -111,
    val icon: String = "",
    val condition: String = "unknown",
    val windSpeed: Double = 0.0,
    val windDir: String = "unknown",
    val pressure: Int = 760,
    val humidity: Int = 0
)

fun getDefaultCity() = City("Moscow", 55.7522, 37.6156)


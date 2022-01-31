package com.example.dailymeteo.repositiry.dto.weather

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    val id: Long,
    @SerializedName("main")
    val condition: String,
    val description: String,
    val icon: String
)

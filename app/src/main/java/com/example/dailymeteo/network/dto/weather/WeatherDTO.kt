package com.example.dailymeteo.network.dto.weather

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("id")
    val conditionId: Long,
    @SerializedName("main")
    val condition: String,
    val description: String,
    val icon: String
)

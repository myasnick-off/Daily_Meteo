package com.example.dailymeteo.network.dto.weather

import com.google.gson.annotations.SerializedName

data class TempDTO(
    @SerializedName("morn")
    val morning: Double,
    val day: Double,
    @SerializedName("eve")
    val evening: Double,
    val night: Double,
    val min: Double,
    val max: Double
)

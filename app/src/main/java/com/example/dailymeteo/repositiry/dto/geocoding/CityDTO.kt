package com.example.dailymeteo.repositiry.dto.geocoding

import com.google.gson.annotations.SerializedName

data class CityDTO(
    val name: String,
    @SerializedName("local_names")
    val localNames: Map<String, String>,
    val lat: Double,
    val lon: Double,
    val country: String
)

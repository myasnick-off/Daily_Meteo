package com.example.dailymeteo.network.dto.weather

data class AllMeteoDataDTO(
    val lat: Double,
    val lon: Double,
    val current: CurrentDTO,
    val daily: List<DailyDTO>
)


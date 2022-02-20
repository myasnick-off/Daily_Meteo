package com.example.dailymeteo.domain.model

data class Weather(
    val city: City,
    val current: Current,
    val daily: List<Daily>
)



package com.example.dailymeteo.domain

data class Weather(
    val city: City = getDefaultCity(),
    val current: Current,
    val daily: List<Daily>
)



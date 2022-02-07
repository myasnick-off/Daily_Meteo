package com.example.dailymeteo.domain

data class Weather(
    val city: City,
    val current: Current,
    val daily: List<Daily>
)



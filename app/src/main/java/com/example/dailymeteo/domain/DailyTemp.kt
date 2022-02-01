package com.example.dailymeteo.domain

data class DailyTemp(
    val morning: Int,
    val day: Int,
    val evening: Int,
    val night: Int,
    val min: Int,
    val max: Int
)

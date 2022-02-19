package com.example.dailymeteo.domain.model

import android.os.Parcelable
import com.example.dailymeteo.utils.WindDir
import kotlinx.parcelize.Parcelize

@Parcelize
data class Daily(
    val time: Long,
    val dayMonth: String,
    val weekDay: String,
    val sunrise: String,
    val sunset: String,
    val temp: DailyTemp,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDir: Int,
    val windDirName: WindDir,
    val cloudiness: Int,
    val uvIndex: Float,
    val precProb: Int,
    val description: String,
    val icon: String
): Parcelable

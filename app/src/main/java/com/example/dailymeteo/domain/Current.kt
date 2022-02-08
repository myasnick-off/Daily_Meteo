package com.example.dailymeteo.domain

import android.os.Parcelable
import com.example.dailymeteo.utils.WindDir
import kotlinx.parcelize.Parcelize

@Parcelize
data class Current(
    val sunrise: String,
    val sunset: String,
    val temp: Int,
    val feelsLike: Int,
    val pressure: Int,
    val humidity: Int,
    val uvIndex: Float,
    val visibility: Int,
    val cloudiness: Int,
    val windSpeed: Double,
    val windGust: Double,
    val windDir: Int,
    val windDirName: WindDir,
    val description: String,
    val icon: String
) : Parcelable {
}
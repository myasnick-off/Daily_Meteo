package com.example.dailymeteo.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val country: String, val lat: Double, val lon: Double): Parcelable

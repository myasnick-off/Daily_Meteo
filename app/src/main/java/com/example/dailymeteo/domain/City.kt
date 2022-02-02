package com.example.dailymeteo.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val lat: Double, val lon: Double): Parcelable


//TODO удалить заглушку
fun getDefaultCity() = City("Moscow", 55.7522, 37.6156)
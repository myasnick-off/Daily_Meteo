package com.example.dailymeteo.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val lat: Double, val lon: Double): Parcelable


//TODO удалить заглушку
fun getDefaultCity() = City("Moscow", 55.7522, 37.6156)

fun getDefaultCityList() = listOf(
    City("Москва", 55.7522, 37.6156),
    City("Минск", 53.9, 27.5667),
    City("Лондон", 51.5085, -0.12574),
    City("Париж", 48.8534, 2.3488),
    City("Стамбул", 41.0138, 28.9497),
    City("Пекин", 39.9075, 116.397),
    City("Санкт-Петербург", 59.9386, 30.3141),
    City("Одесса", 46.4775, 30.7326),
    City("Омск", 54.9924, 73.3686),
    City("Новосибирск", 55.0415, 82.9346)
)
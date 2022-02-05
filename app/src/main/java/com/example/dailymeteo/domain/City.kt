package com.example.dailymeteo.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val country: String, val lat: Double, val lon: Double): Parcelable


//TODO удалить заглушку
fun getDefaultCity() = City("Moscow", "RU",55.7522, 37.6156)

fun getDefaultCityList() = listOf(
    City("Москва", "RU",55.7522, 37.6156),
    City("Минск", "RU",53.9, 27.5667),
    City("Лондон", "RU",51.5085, -0.12574),
    City("Париж", "RU",48.8534, 2.3488),
    City("Стамбул", "RU",41.0138, 28.9497),
    City("Пекин", "RU",39.9075, 116.397),
    City("Санкт-Петербург", "RU",59.9386, 30.3141),
    City("Одесса", "RU",46.4775, 30.7326),
    City("Омск", "RU",54.9924, 73.3686),
    City("Новосибирск", "RU",55.0415, 82.9346)
)
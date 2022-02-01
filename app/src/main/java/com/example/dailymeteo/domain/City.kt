package com.example.dailymeteo.domain

data class City(val name: String, val lat: Double, val lon: Double)


//TODO удалить заглушку
fun getDefaultCity() = City("Moscow", 55.7522, 37.6156)
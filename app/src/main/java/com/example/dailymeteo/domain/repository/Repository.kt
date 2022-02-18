package com.example.dailymeteo.domain.repository

import com.example.dailymeteo.network.dto.geocoding.CityDTO
import com.example.dailymeteo.network.dto.weather.AllMeteoDataDTO
import retrofit2.Callback

interface Repository {
    fun getAllMeteoDataFromServer(lat: Double, lon: Double, callback: Callback<AllMeteoDataDTO>)
    fun getGeoDataFromServer(cityName: String, callback: Callback<List<CityDTO>>)
}
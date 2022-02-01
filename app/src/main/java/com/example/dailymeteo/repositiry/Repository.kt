package com.example.dailymeteo.repositiry

import com.example.dailymeteo.domain.Weather
import com.example.dailymeteo.repositiry.dto.geocoding.LocationsDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import retrofit2.Callback

interface Repository {
    fun getAllMeteoDataFromServer(lat: Double, lon: Double, callback: Callback<AllMeteoDataDTO>)
    fun getGeoDataFromServer(cityName: String, callback: Callback<LocationsDTO>)
}
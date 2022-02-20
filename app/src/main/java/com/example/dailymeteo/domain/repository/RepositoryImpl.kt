package com.example.dailymeteo.domain.repository

import com.example.dailymeteo.network.RemoteDataSource
import com.example.dailymeteo.network.dto.geocoding.CityDTO
import com.example.dailymeteo.network.dto.weather.AllMeteoDataDTO
import retrofit2.Callback

class RepositoryImpl(private val remoteDataSource: RemoteDataSource): Repository {

    override fun getAllMeteoDataFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<AllMeteoDataDTO>
    ) {
        remoteDataSource.getWeatherData(lat, lon, callback)
    }

    override fun getGeoDataFromServer(cityName: String, callback: Callback<List<CityDTO>>) {
        remoteDataSource.getGeoData(cityName, callback)
    }
}
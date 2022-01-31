package com.example.dailymeteo.repositiry

import com.example.dailymeteo.domain.Weather
import com.example.dailymeteo.repositiry.dto.geocoding.LocationsDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import retrofit2.Callback

class RepositoryImpl(private val remoteDataSource: RemoteDataSource): Repository {

    override fun getAllMeteoDataFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<AllMeteoDataDTO>
    ) {
        remoteDataSource.getWeatherData(lat, lon, callback)
    }

    override fun getGeoDataFromServer(cityName: String, callback: Callback<LocationsDTO>) {
        remoteDataSource.getGeoData(cityName, callback)
    }

    //TODO удалить заглушку
    override fun getDataFromServer(): Weather {
        return Weather()
    }
}
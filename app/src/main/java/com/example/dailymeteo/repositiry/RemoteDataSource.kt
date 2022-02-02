package com.example.dailymeteo.repositiry

import com.example.dailymeteo.repositiry.dto.geocoding.CityDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import com.example.dailymeteo.utils.OPEN_WEATHER_API_URL
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val weatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    fun getWeatherData(lat: Double, lon: Double, callback: Callback<AllMeteoDataDTO>) {
        weatherAPI.getWeather(lat = lat, lon = lon).enqueue(callback)
    }

    fun getGeoData(cityName: String, callback: Callback<List<CityDTO>>) {
        weatherAPI.getGeocoding(cityName = cityName).enqueue(callback)
    }

}
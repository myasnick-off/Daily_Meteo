package com.example.dailymeteo.repositiry

import com.example.dailymeteo.BuildConfig
import com.example.dailymeteo.repositiry.dto.geocoding.LocationsDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import com.example.dailymeteo.utils.GEOCODING_END_POINT
import com.example.dailymeteo.utils.WEATHER_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    // запрос на получение полных данных о погоде (текущие и прогноз на неделю)
    @GET(WEATHER_END_POINT)
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Call<AllMeteoDataDTO>

    // запрос на получение геоданных населенного пункта по его названию
    @GET(GEOCODING_END_POINT)
    fun getGeocoding(
        @Query("q") cityName: String,
        @Query("limit") resultsLimit: Int = 1,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ):Call<LocationsDTO>
}
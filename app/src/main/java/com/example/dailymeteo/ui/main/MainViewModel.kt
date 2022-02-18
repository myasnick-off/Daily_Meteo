package com.example.dailymeteo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailymeteo.domain.model.City
import com.example.dailymeteo.network.RemoteDataSource
import com.example.dailymeteo.domain.repository.Repository
import com.example.dailymeteo.domain.repository.RepositoryImpl
import com.example.dailymeteo.network.dto.weather.AllMeteoDataDTO
import com.example.dailymeteo.utils.convertDTOtoWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData(): LiveData<MainAppState> = liveData

    fun getWeatherFromRemoteSource(city: City) {
        liveData.value = MainAppState.Loading
        repository.getAllMeteoDataFromServer(city.lat, city.lon, object : Callback<AllMeteoDataDTO> {

            override fun onResponse(call: Call<AllMeteoDataDTO>, response: Response<AllMeteoDataDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    val weather = convertDTOtoWeather(response.body()!!, city)
                    liveData.postValue(MainAppState.Success(weather))
                } else {
                    liveData.postValue(MainAppState.Error(IllegalStateException()))
                }
            }
            override fun onFailure(call: Call<AllMeteoDataDTO>, t: Throwable) {
                liveData.postValue(MainAppState.Error(IllegalStateException()))
            }
        })
    }
}
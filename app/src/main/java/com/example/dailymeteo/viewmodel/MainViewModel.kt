package com.example.dailymeteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailymeteo.repositiry.Repository
import com.example.dailymeteo.repositiry.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<MainAppState> = liveData

    fun getWeatherData() {
        liveData.value = MainAppState.Loading
        Thread() {
            sleep(2000)
            liveData.postValue(MainAppState.Success(repository.getDataFromServer()))
        }.start()
    }
}
package com.example.dailymeteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailymeteo.App.Companion.getHistoryDAO
import com.example.dailymeteo.domain.City
import com.example.dailymeteo.repositiry.LocalRepository
import com.example.dailymeteo.repositiry.dto.LocalRepositoryImpl
import com.example.dailymeteo.utils.convertCityToEntity

class HistoryViewModel(
    private val liveData: MutableLiveData<HistoryAppState> = MutableLiveData(),
    private val localRepository: LocalRepository = LocalRepositoryImpl(getHistoryDAO())
) :
    ViewModel() {

    fun getLiveData(): LiveData<HistoryAppState> = liveData

    fun getHistoryFromLocalSource() {
        liveData.value = HistoryAppState.Loading
        Thread {
            val history = localRepository.getAllHistory()
            liveData.postValue(HistoryAppState.Success(history))
        }.start()
    }

    fun saveCityToLocalSource(city: City) {
        Thread {
            localRepository.saveEntity(city)
        }.start()
    }
}
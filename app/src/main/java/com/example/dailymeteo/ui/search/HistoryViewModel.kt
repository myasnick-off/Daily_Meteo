package com.example.dailymeteo.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailymeteo.App.Companion.getHistoryDAO
import com.example.dailymeteo.domain.model.City
import com.example.dailymeteo.domain.repository.LocalRepository
import com.example.dailymeteo.network.dto.LocalRepositoryImpl

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

    fun removeAllHistory() {
        liveData.value = HistoryAppState.Loading
        Thread {
            localRepository.clearHistory()
            liveData.postValue(HistoryAppState.Success(localRepository.getAllHistory()))
        }.start()
    }
}
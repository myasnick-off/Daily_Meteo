package com.example.dailymeteo.viewmodel

import com.example.dailymeteo.domain.Weather

sealed class MainAppState {
    object Loading: MainAppState()
    data class Success(val weatherData: Weather): MainAppState()
    data class Error(val error: Throwable): MainAppState()
}
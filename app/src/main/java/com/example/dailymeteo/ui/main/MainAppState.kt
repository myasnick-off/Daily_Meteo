package com.example.dailymeteo.ui.main

import com.example.dailymeteo.domain.model.Weather

sealed class MainAppState {
    object Loading: MainAppState()
    data class Success(val weatherData: Weather): MainAppState()
    data class Error(val error: Throwable): MainAppState()
}
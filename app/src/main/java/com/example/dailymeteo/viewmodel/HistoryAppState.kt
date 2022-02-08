package com.example.dailymeteo.viewmodel

import com.example.dailymeteo.domain.City

sealed class HistoryAppState {
    object Loading : HistoryAppState()
    data class Success(val history: List<City>): HistoryAppState()
}
package com.example.dailymeteo.ui.search

import com.example.dailymeteo.domain.model.City

sealed class HistoryAppState {
    object Loading : HistoryAppState()
    data class Success(val history: List<City>): HistoryAppState()
}
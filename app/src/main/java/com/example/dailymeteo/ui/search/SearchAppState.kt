package com.example.dailymeteo.ui.search

import com.example.dailymeteo.domain.model.City

sealed class SearchAppState {
    object Loading : SearchAppState()
    data class Success(val city: City): SearchAppState()
    data class Error(val error: Int): SearchAppState()
}
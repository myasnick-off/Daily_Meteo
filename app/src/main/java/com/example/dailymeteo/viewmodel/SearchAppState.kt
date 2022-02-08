package com.example.dailymeteo.viewmodel

import com.example.dailymeteo.domain.City

sealed class SearchAppState {
    object Loading : SearchAppState()
    data class Success(val city: City): SearchAppState()
    data class Error(val error: Int): SearchAppState()
}
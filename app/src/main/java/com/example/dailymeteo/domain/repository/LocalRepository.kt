package com.example.dailymeteo.domain.repository

import com.example.dailymeteo.domain.model.City

interface LocalRepository {
    fun getAllHistory(): List<City>
    fun clearHistory()
    fun saveEntity(city: City)
    fun removeEntity(city: City)
}
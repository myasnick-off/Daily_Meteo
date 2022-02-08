package com.example.dailymeteo.repositiry

import com.example.dailymeteo.domain.City

interface LocalRepository {
    fun getAllHistory(): List<City>
    fun clearHistory()
    fun saveEntity(city: City)
    fun removeEntity(city: City)
}
package com.example.dailymeteo.repositiry.dto

import com.example.dailymeteo.domain.City
import com.example.dailymeteo.repositiry.LocalRepository
import com.example.dailymeteo.room.HistoryDAO
import com.example.dailymeteo.utils.convertCityToEntity
import com.example.dailymeteo.utils.convertEntityToCity
import java.util.*

class LocalRepositoryImpl(private val localDataSource: HistoryDAO) : LocalRepository {

    override fun getAllHistory(): List<City> {
        return localDataSource.getAll().map { convertEntityToCity(it) }
    }

    override fun clearHistory() {
        localDataSource.deleteAll()
    }

    override fun saveEntity(city: City) {
        val history = localDataSource.getByName(city.name)      // ищем аналогичный город в истории
        if (history.isNotEmpty()) {                             // если такой город уже есть:
            history.first().apply {
                date = Date().time                              // обновляем в нем дату поиска
                localDataSource.insert(this)              // и перезаписываем его в истории
            }
        } else {                                                // если такого города еще нет:
            localDataSource.insert(convertCityToEntity(city))   // просто добавляем в историю
        }

    }

    override fun removeEntity(city: City) {
        localDataSource.delete(convertCityToEntity(city))
    }
}
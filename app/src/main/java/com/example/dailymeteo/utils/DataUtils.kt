package com.example.dailymeteo.utils

import com.example.dailymeteo.domain.*
import com.example.dailymeteo.repositiry.dto.geocoding.CityDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import com.example.dailymeteo.repositiry.dto.weather.DailyDTO
import com.example.dailymeteo.room.HistoryEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

fun convertEntityToCity(entity: HistoryEntity): City {
    return City(entity.cityName, entity.country, entity.lat, entity.lon)
}

fun convertCityToEntity(city: City): HistoryEntity {
    return HistoryEntity(0, Date().time, city.name, city.country, city.lat, city.lon)
}

fun convertDTOtoCity(locationsDTO: List<CityDTO>): City {
    return City(
        name = if (locationsDTO.first().localNames.containsKey("ru")) locationsDTO.first().localNames["ru"]!! else locationsDTO.first().name,
        locationsDTO.first().country,
        locationsDTO.first().lat,
        locationsDTO.first().lon
    )
}

fun convertDTOtoWeather(meteoDataDTO: AllMeteoDataDTO, city: City): Weather {
    return Weather(
        City(city.name, city.country, meteoDataDTO.lat, meteoDataDTO.lon),
        Current(
            getTimeFromDate(meteoDataDTO.current.sunrise),
            getTimeFromDate(meteoDataDTO.current.sunset),
            meteoDataDTO.current.temp.toInt(),
            meteoDataDTO.current.feelsLike.toInt(),
            (meteoDataDTO.current.pressure * PRESSURE_INDEX).toInt(),
            meteoDataDTO.current.humidity,
            meteoDataDTO.current.uvIndex,
            meteoDataDTO.current.visibility,
            meteoDataDTO.current.cloudiness,
            meteoDataDTO.current.windSpeed,
            meteoDataDTO.current.windGust,
            meteoDataDTO.current.windDir + DIR_CORRECTION,
            convertDegreeToDirection(meteoDataDTO.current.windDir),
            meteoDataDTO.current.weather.first().description,
            meteoDataDTO.current.weather.first().icon
        ),
        meteoDataDTO.daily.map { convertDTOtoDailyWeather(it) }
    )
}

private fun convertDTOtoDailyWeather(dailyDTO: DailyDTO): Daily {
    return Daily(
        getDayMonthFromDate(dailyDTO.time),
        getWeekDayFromDate(dailyDTO.time),
        getTimeFromDate(dailyDTO.sunrise),
        getTimeFromDate(dailyDTO.sunset),
        DailyTemp(
            dailyDTO.temp.morning.toInt(),
            dailyDTO.temp.day.toInt(),
            dailyDTO.temp.evening.toInt(),
            dailyDTO.temp.night.toInt(),
            dailyDTO.temp.min.toInt(),
            dailyDTO.temp.max.toInt()
        ),
        (dailyDTO.pressure * PRESSURE_INDEX).toInt(),
        dailyDTO.humidity,
        dailyDTO.windSpeed,
        convertDegreeToDirection(dailyDTO.windDir),
        dailyDTO.cloudiness,
        dailyDTO.uvIndex,
        (dailyDTO.precProb * 100).toInt(),
        dailyDTO.weather.first().description,
        dailyDTO.weather.first().icon
    )
}

// преобразование времени в строковый формат
private fun getTimeFromDate(time: Long): String {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormatter.format(Date(time * MS_IN_SEC))
}

// преобразование даты в строковый формат
private fun getDayMonthFromDate(time: Long): String {
    val timeFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    return timeFormatter.format(Date(time * MS_IN_SEC))
}

// получение дня недели из даты
private fun getWeekDayFromDate(time: Long): String {
    val timeFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return timeFormatter.format(Date(time * MS_IN_SEC))
}

// преобразование градусов в аббривиатуры направлений ветра
private fun convertDegreeToDirection(deg: Int): WindDir {
    if (deg < 23) return WindDir.N
    if (deg < 68) return WindDir.NE
    if (deg < 113) return WindDir.E
    if (deg < 158) return WindDir.SE
    if (deg < 203) return WindDir.S
    if (deg < 248) return WindDir.SW
    if (deg < 293) return WindDir.W
    if (deg < 338) return WindDir.NW
    return WindDir.N
}



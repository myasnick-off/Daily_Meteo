package com.example.dailymeteo.utils

import com.example.dailymeteo.domain.*
import com.example.dailymeteo.repositiry.dto.geocoding.CityDTO
import com.example.dailymeteo.repositiry.dto.weather.AllMeteoDataDTO
import com.example.dailymeteo.repositiry.dto.weather.DailyDTO
import java.text.SimpleDateFormat
import java.util.*

fun convertDTOtoCity(locationsDTO: List<CityDTO>): City {
    return City(
        name = if (locationsDTO.first().localNames.containsKey("ru")) locationsDTO.first().localNames["ru"]!! else locationsDTO.first().name,
        locationsDTO.first().lat,
        locationsDTO.first().lon
    )
}

fun convertDTOtoWeather(meteoDataDTO: AllMeteoDataDTO, cityName: String): Weather {
    return Weather(
        City(cityName, meteoDataDTO.lat, meteoDataDTO.lon),
        Current(
            getTimeFromDate(meteoDataDTO.current.sunrise),
            getTimeFromDate(meteoDataDTO.current.sunset),
            meteoDataDTO.current.temp.toInt(),
            meteoDataDTO.current.feelsLike.toInt(),
            (meteoDataDTO.current.pressure * PRESSURE_INDEX).toInt(),
            meteoDataDTO.current.humidity,
            meteoDataDTO.current.uvIndex,
            meteoDataDTO.current.cloudiness,
            meteoDataDTO.current.windSpeed,
            convertDegreeToDirection(meteoDataDTO.current.windDir),
            meteoDataDTO.current.weather.first().description,
            meteoDataDTO.current.weather.first().icon
        ),
        meteoDataDTO.daily.map { convertDTOtoDailyWeather(it) }
    )
}

private fun convertDTOtoDailyWeather(daily: DailyDTO): Daily {
    return Daily(
        getDayMonthFromDate(daily.time),
        getWeekDayFromDate(daily.time),
        getTimeFromDate(daily.sunrise),
        getTimeFromDate(daily.sunset),
        DailyTemp(
            daily.temp.morning.toInt(),
            daily.temp.day.toInt(),
            daily.temp.evening.toInt(),
            daily.temp.night.toInt(),
            daily.temp.min.toInt(),
            daily.temp.max.toInt()
        ),
        daily.pressure,
        daily.humidity,
        daily.windSpeed,
        convertDegreeToDirection(daily.windDir),
        daily.cloudiness,
        daily.uvIndex,
        (daily.precProb * 100).toInt(),
        daily.weather.first().description,
        daily.weather.first().icon
    )
}

// преобразование времени в строковый формат
private fun getTimeFromDate(time: Long): String {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateTime = Date(time)
    return timeFormatter.format(dateTime)
}

// преобразование даты в строковый формат
private fun getDayMonthFromDate(date: Long): String {
    val timeFormatter = SimpleDateFormat("dd.MM", Locale.getDefault())
    val dateTime = Date(date)
    return timeFormatter.format(dateTime)
}

// получение дня недели из даты
private fun getWeekDayFromDate(date: Long): String {
    val timeFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateTime = Date(date)
    return timeFormatter.format(dateTime)
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



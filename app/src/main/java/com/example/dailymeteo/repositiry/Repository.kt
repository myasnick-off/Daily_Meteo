package com.example.dailymeteo.repositiry

interface Repository {
    fun getDataFromServer(): Weather
}
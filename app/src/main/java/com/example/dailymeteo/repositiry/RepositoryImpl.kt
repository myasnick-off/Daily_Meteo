package com.example.dailymeteo.repositiry

class RepositoryImpl: Repository {

    override fun getDataFromServer(): Weather {
        return Weather()
    }
}
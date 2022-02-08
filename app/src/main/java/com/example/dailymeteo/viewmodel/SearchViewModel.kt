package com.example.dailymeteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailymeteo.repositiry.RemoteDataSource
import com.example.dailymeteo.repositiry.Repository
import com.example.dailymeteo.repositiry.RepositoryImpl
import com.example.dailymeteo.repositiry.dto.geocoding.CityDTO
import com.example.dailymeteo.utils.SEARCH_FAILURE
import com.example.dailymeteo.utils.SEARCH_NO_RESULTS
import com.example.dailymeteo.utils.SEARCH_UNSUCCESSFUL
import com.example.dailymeteo.utils.convertDTOtoCity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(
    private val liveData: MutableLiveData<SearchAppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData(): LiveData<SearchAppState> = liveData

    fun getGeocodingFromRemoteSource(cityName: String) {
        liveData.value = SearchAppState.Loading
        repository.getGeoDataFromServer(cityName, object : Callback<List<CityDTO>> {

            override fun onResponse(call: Call<List<CityDTO>>, response: Response<List<CityDTO>>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.isNotEmpty()) {
                        val city = convertDTOtoCity(response.body()!!)
                        liveData.postValue(SearchAppState.Success(city))
                    } else {
                        liveData.postValue(SearchAppState.Error(SEARCH_NO_RESULTS))
                    }
                } else {
                    liveData.postValue(SearchAppState.Error(SEARCH_UNSUCCESSFUL))
                }
            }
            override fun onFailure(call: Call<List<CityDTO>>, t: Throwable) {
                liveData.postValue(SearchAppState.Error(SEARCH_FAILURE))
            }
        })
    }
}
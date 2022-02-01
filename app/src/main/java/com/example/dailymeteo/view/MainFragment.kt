package com.example.dailymeteo.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentMainBinding
import com.example.dailymeteo.domain.Weather
import com.example.dailymeteo.domain.getDefaultCity
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.viewmodel.MainAppState
import com.example.dailymeteo.viewmodel.MainViewModel

class MainFragment: Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
    get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = Observer<MainAppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        requestData()
    }

    private fun renderData(appState: MainAppState) = with(binding) {
        when(appState){
            MainAppState.Loading -> mainProgressBar.show()
            is MainAppState.Success -> {
                mainProgressBar.hide()
                showData(appState.weatherData)
            }
            is MainAppState.Error -> {
                mainProgressBar.hide()
                //TODO добавить обработку ошибки
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun requestData(){
        viewModel.getWeatherFromRemoteSource(getDefaultCity())
    }

    @SuppressLint("SetTextI18n")
    private fun showData(weather: Weather) = with(binding) {
        tempTextView.text = "${weather.current.temp} ${getString(R.string.temp_val)}"
        feelsTempTextView.text = "${weather.current.feelsLike} ${getString(R.string.temp_val)}"
        humidityTextView.text = "${weather.current.humidity} ${getString(R.string.humidity_val)}"
        pressureTextView.text = "${weather.current.pressure} ${getString(R.string.pressure_val)}"
        windTextView.text = "${weather.current.windSpeed} ${getString(R.string.speed_val)}"
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
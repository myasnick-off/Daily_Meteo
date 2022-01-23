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
import com.example.dailymeteo.hide
import com.example.dailymeteo.repositiry.Weather
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
        viewModel.getWeatherData()
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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showData(weatherData: Weather) = with(binding) {
        tempTextView.text = "${weatherData.temp} ${getString(R.string.temp_val)}"
        feelsTempTextView.text = "${weatherData.feelsLike} ${getString(R.string.temp_val)}"
        humidityTextView.text = "${weatherData.humidity} ${getString(R.string.humidity_val)}"
        pressureTextView.text = "${weatherData.pressure} ${getString(R.string.pressure_val)}"
        windTextView.text = "${weatherData.windSpeed} ${getString(R.string.speed_val)}"
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
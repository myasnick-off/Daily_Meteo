package com.example.dailymeteo.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentMainBinding
import com.example.dailymeteo.domain.City
import com.example.dailymeteo.domain.Daily
import com.example.dailymeteo.domain.Weather
import com.example.dailymeteo.domain.getDefaultCity
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.utils.ARG_CITY_NAME
import com.example.dailymeteo.utils.ICON_BASE_URL
import com.example.dailymeteo.utils.ICON_EXT
import com.example.dailymeteo.utils.ICON_LARGE
import com.example.dailymeteo.view.MainActivity
import com.example.dailymeteo.view.search.SearchFragment
import com.example.dailymeteo.viewmodel.MainAppState
import com.example.dailymeteo.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment: Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var currentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentCity = it.getParcelable(ARG_CITY_NAME)
        }
    }

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
        appbarInit()

        val observer = Observer<MainAppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        if (currentCity != null) {
            requestData(currentCity!!)
        } else {
            requestData(getDefaultCity())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container,SearchFragment.newInstance(), "")
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun appbarInit() {
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(binding.mainToolbar)
        setHasOptionsMenu(true)
        // TODO bottomSheetBehavior
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
                Snackbar.make(binding.root, "Some failure!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestData(city: City){
        viewModel.getWeatherFromRemoteSource(city)
    }

    @SuppressLint("SetTextI18n")
    private fun showData(weather: Weather) = with(binding) {
        cityTextView.text = weather.city.name
        tempTextView.text = "${weather.current.temp} ${getString(R.string.temp_val)}"
        feelsTempTextView.text = "${getString(R.string.feels_like)} ${weather.current.feelsLike} ${getString(R.string.temp_val)}"
        humidityTextView.text = "${weather.current.humidity} ${getString(R.string.humidity_val)}"
        pressureTextView.text = "${weather.current.pressure} ${getString(R.string.pressure_val)}"
        windTextView.text = "${weather.current.windSpeed} ${getString(R.string.speed_val)}"
        conditionImageView.load("$ICON_BASE_URL${weather.current.icon}$ICON_LARGE$ICON_EXT")
        initRecyclerView(weather.daily)
    }

    private fun initRecyclerView(daily: List<Daily>) {
        val adapter = MainRecyclerAdapter().apply {
            setItems(daily)
        }
        binding.mainRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(args: Bundle?): MainFragment {
            return MainFragment().apply {
                this.arguments = args
            }
        }
    }
}
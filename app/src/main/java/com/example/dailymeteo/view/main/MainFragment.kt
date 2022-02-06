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
import com.example.dailymeteo.domain.*
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.utils.*
import com.example.dailymeteo.view.MainActivity
import com.example.dailymeteo.view.details.DetailsFragment
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
                showWeather(appState.weatherData)
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
    private fun showWeather(weather: Weather) = with(binding) {
        cityTextView.text = weather.city.name
        current.tempTextView.text = "${weather.current.temp}"
        current.feelsTempTextView.text = "${weather.current.feelsLike} ${getString(R.string.temp_unit)}"
        current.conditionTextView.text = weather.current.description
        current.conditionImageView.load("$ICON_BASE_URL${weather.current.icon}$ICON_LARGE$ICON_EXT")
        current.root.setOnClickListener { lunchDetails(weather.current, weather.city.name) }
        initRecyclerView(weather.daily)
    }

    private fun lunchDetails(current: Current, cityName: String) {
        val args = Bundle().apply {
            putParcelable(ARG_CURRENT, current)
            putString(ARG_CITY_DETAILS, cityName)
        }
        parentFragmentManager.beginTransaction()
            .add(R.id.main_container, DetailsFragment.newInstance(args), "")
            .addToBackStack("DetailsFragment")
            .commit()
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
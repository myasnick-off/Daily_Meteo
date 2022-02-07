package com.example.dailymeteo.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if(result) getMyLocation()
        else showRationaleDialog()
    }

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

        if (currentCity != null) {          // если город выбран:
            requestMeteoData(currentCity!!) // запрашивем метооданные по его координатам
        } else {                            // если город не выбран (обычно при запуске приложения):
            getMyLocation()                 // определяем координаты по геолокации устройства
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
            MainAppState.Loading -> {
                mainProgressBar.show()
                mainLayout.hide()
            }
            is MainAppState.Success -> {
                mainProgressBar.hide()
                mainLayout.show()
                showWeather(appState.weatherData)
            }
            is MainAppState.Error -> {
                mainProgressBar.hide()
                Snackbar.make(binding.root, "Some failure!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestMeteoData(city: City){
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

    // функция определения геолокации устройства
    private fun getMyLocation() {
        context?.let { ct ->
            // проверяем наличие разрешения на использование грубой геолокации устройства
            if(ContextCompat.checkSelfPermission(ct, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = ct.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                // получаем последнюю изывестную геолокацию устройства
                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let {
                    // если геолокация получена, запускаем по ней поиск адреса
                    getAddressAsync(ct, location)
                }
                // сразу же инициируем запрос актуальной геолокации у наиболее подходящего LocationProvider'a
                val provider = locationManager.getBestProvider(Criteria(), true)
                provider?.let {
                    locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE) { location ->
                        // как только геолокация получена, запускаем по ней поиск адреса
                        getAddressAsync(ct, location) }
                }
            } else {
                // если разрешения на использование геолокации нет, запрашиваем его у пользователя
                checkPermission()
            }
        }
    }

    // функция поиска адреса по данным геолокации
    private fun getAddressAsync(context: Context, location: Location) {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val city = City(
            address.first().locality
                ?: address.first().subLocality
                ?: address.first().adminArea
                ?: address.first().subAdminArea ?: "",
            address.first().countryName ?: "",
            location.latitude,
            location.longitude
        )
        if (currentCity != null) {                  // если город уже найден по последним известным геоданным:
            if (currentCity!!.name != city.name) {  // если название города по актуальным геоданным отличается от текущего:
                currentCity = city                  // перезаписываем текущий город на актуральный
                requestMeteoData(currentCity!!)     // запрашиеваем метеоданные для актуального города
            }
        } else {                                    // если текущий город еще не определен:
            currentCity = city                      // сохраняем город, найденнный по последним известным геоданным
            requestMeteoData(currentCity!!)         // запрашиеваем метеоданные для этого города
        }



    }

    private fun checkPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            showRationaleDialog()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_access_required)
            .setMessage(R.string.to_display_weather_in_your_location_location_access_required)
            .setPositiveButton(R.string.accept) { _, _ -> requestPermission() }
            .setNegativeButton(R.string.deny) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    companion object {
        fun newInstance(args: Bundle?): MainFragment {
            return MainFragment().apply {
                this.arguments = args
            }
        }
    }
}
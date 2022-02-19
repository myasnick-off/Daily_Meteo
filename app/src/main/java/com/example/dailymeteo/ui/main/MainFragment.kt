package com.example.dailymeteo.ui.main

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
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentMainBinding
import com.example.dailymeteo.domain.model.City
import com.example.dailymeteo.domain.model.Current
import com.example.dailymeteo.domain.model.Daily
import com.example.dailymeteo.domain.model.Weather
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.ui.MainActivity
import com.example.dailymeteo.ui.daily.DailyFragment
import com.example.dailymeteo.ui.details.DetailsFragment
import com.example.dailymeteo.ui.search.SearchFragment
import com.example.dailymeteo.utils.*
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
                runSearchScreen()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun appbarInit() {
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(binding.mainToolbar)
        setHasOptionsMenu(true)
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
        current.root.setOnClickListener { runDetailsScreen(weather.current, weather.city.name) }
        initRecyclerView(weather.daily)
    }

    private fun runDetailsScreen(current: Current, cityName: String) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_container, DetailsFragment.newInstance(current, cityName), "")
            .addToBackStack("DetailsFragment")
            .commit()
    }

    private fun runDailyScreen(daily: List<Daily>) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_container, DailyFragment.newInstance(daily), "")
            .addToBackStack("DailyFragment")
            .commit()
    }

    private fun runSearchScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SearchFragment.newInstance(), "")
            .commit()
    }

    private fun initRecyclerView(daily: List<Daily>) {
        val adapter = MainRecyclerAdapter().apply {
            submitList(daily)
            setListener(object : ItemClickListener {
                override fun onItemClicked() { runDailyScreen(daily) }
            })
        }
        binding.mainRecyclerView.adapter = adapter
    }

    // функция определения геолокации устройства
    private fun getMyLocation() {
        context?.let {
            // проверяем наличие разрешения на использование грубой геолокации устройства
            if(ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = it.getSystemService<LocationManager>()
                // получаем последнюю изывестную геолокацию устройства
                val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                // запускаем поиск адреса по геолокации
                getAddress(it, location)
                // сразу же инициируем запрос актуальной геолокации у наиболее подходящего LocationProvider'a
                val provider = locationManager?.getBestProvider(Criteria(), true)
                provider?.let {
                    locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE) { location ->
                        // как только геолокация получена, сохраняем координаты в настройках прложения
                        saveLocation(location) }
                }
            } else {
                // если разрешения на использование геолокации нет, запрашиваем его у пользователя
                checkPermission()
            }
        }
    }

    // функция поиска адреса по данным геолокации
    private fun getAddress(context: Context, location: Location?) {
        // если последняя локация не найдена загружаем координаты из настроек приложения
        val lat = location?.latitude ?: loadLocation(KEY_LAT)
        val lon = location?.longitude ?: loadLocation(KEY_LON)
        // ищем адрес по полученным координатам
        val address = Geocoder(context).getFromLocation(lat, lon, 1)
        if (address != null && address.isNotEmpty()) {
            val city = City(
                address.first().locality
                    ?: address.first().subLocality
                    ?: address.first().adminArea
                    ?: address.first().subAdminArea ?: "",
                address.first().countryName ?: "",
                lat,
                lon
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
        } else {
            showErrorDialog()
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
            .setIcon(R.drawable.ic_baseline_warning_amber_24)
            .setMessage(R.string.to_display_weather_in_your_location_location_access_required)
            .setPositiveButton(R.string.accept) { _, _ -> requestPermission() }
            .setNegativeButton(R.string.deny) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setMessage(R.string.cant_find_location)
            .setPositiveButton(R.string.search) { _, _ ->  runSearchScreen()}
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun saveLocation(location: Location) {
        context?.let {
            val sharedPref = it.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
            sharedPref.edit().apply {
                putFloat(KEY_LAT, location.latitude.toFloat())
                putFloat(KEY_LON, location.longitude.toFloat())
            }
        }
    }

    private fun loadLocation(key: String):Double {
        var lat = 1000f
        context?.let {
            val sharedPref = it.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
            lat = sharedPref.getFloat(key, 1000f)
        }
        return lat.toDouble()
    }

    interface ItemClickListener {
        fun onItemClicked()
    }

    companion object {
        fun newInstance(args: Bundle?): MainFragment {
            return MainFragment().apply {
                this.arguments = args
            }
        }
    }
}
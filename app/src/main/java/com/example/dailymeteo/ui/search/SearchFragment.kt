package com.example.dailymeteo.ui.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentSearchBinding
import com.example.dailymeteo.domain.model.City
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.ui.BackPressedMonitor
import com.example.dailymeteo.ui.MainActivity
import com.example.dailymeteo.ui.main.MainFragment
import com.example.dailymeteo.utils.ARG_CITY_NAME
import com.example.dailymeteo.utils.SEARCH_FAILURE
import com.example.dailymeteo.utils.SEARCH_NO_RESULTS
import com.example.dailymeteo.utils.SEARCH_UNSUCCESSFUL
import com.google.android.material.snackbar.Snackbar

class SearchFragment: Fragment(), BackPressedMonitor {

    private val searchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private val historyViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { SearchRecyclerAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        appbarInit()
        listInit()

        searchViewModel.getLiveData().observe(viewLifecycleOwner, { renderSearchData(it) })
        historyViewModel.getLiveData().observe(viewLifecycleOwner, {renderHistoryData(it)})
        requestHistory()

        searchInputLayout.setEndIconOnClickListener {
            val cityName = binding.searchEditText.text.toString()
            requestCity(cityName)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_clear -> {
                clearHistory()
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
        mainActivity.setSupportActionBar(binding.searchToolbar)
        setHasOptionsMenu(true)
    }

    private fun listInit() {
        adapter.setListener(object : ItemClickListener {
            override fun onItemClicked(pos: Int) {
                val city = adapter.getItems()[pos]
                saveCityToHistory(city)     // перезаписываем в истории для обновления даты запроса
                showWeather(city)
            }
        })
        binding.searchRecyclerView.adapter = adapter
    }

    private fun renderHistoryData(appState: HistoryAppState) = with(binding) {
        when(appState) {
            HistoryAppState.Loading -> searchProgressBar.show()
            is HistoryAppState.Success -> {
                searchProgressBar.hide()
                showCities(appState.history)
            }
        }
    }

    private fun renderSearchData(appState: SearchAppState) = with(binding) {
        when(appState) {
            SearchAppState.Loading -> searchProgressBar.show()
            is SearchAppState.Success -> {
                searchProgressBar.hide()
                saveCityToHistory(appState.city)
                showWeather(appState.city)
            }
            is SearchAppState.Error -> {
                searchProgressBar.hide()
                showErrorMessage(appState.error)
            }
        }
    }

    private fun requestHistory() {
        historyViewModel.getHistoryFromLocalSource()
    }

    private fun clearHistory() {
        historyViewModel.removeAllHistory()
    }

    private fun showCities(history: List<City>) {
        adapter.submitList(history)

    }

    private fun requestCity(cityName: String) {
        searchViewModel.getGeocodingFromRemoteSource(cityName)
    }

    private fun saveCityToHistory(city: City) {
        historyViewModel.saveCityToLocalSource(city)
    }

    private fun showWeather(city: City) {
        val args = Bundle().apply {
            putParcelable(ARG_CITY_NAME, city)
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment.newInstance(args), "")
            .commit()
    }

    // функция обработки кодов ошибок
    private fun showErrorMessage(error: Int) {
        when(error) {
            SEARCH_NO_RESULTS -> {
                Snackbar.make(binding.root, "Nothing found!", Snackbar.LENGTH_SHORT).show()
            }
            SEARCH_UNSUCCESSFUL -> {
                Snackbar.make(binding.root, "Unsuccessful result!", Snackbar.LENGTH_SHORT).show()
            }
            SEARCH_FAILURE -> {
                Snackbar.make(binding.root, "Search failure!", Snackbar.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // обработка события по нажатию кнопки "Назад" во фрагменте
    override fun onBackPressed(): Boolean {
        // опустошаем backStack и переходим на последний в бэкстеке фрагмент (MainFragment)
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return true
    }

    // интерфейс для обработки кликов на элементы списка
    interface ItemClickListener {
        fun onItemClicked(pos: Int)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
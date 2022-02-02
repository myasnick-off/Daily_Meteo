package com.example.dailymeteo.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentSearchBinding
import com.example.dailymeteo.domain.City
import com.example.dailymeteo.hide
import com.example.dailymeteo.show
import com.example.dailymeteo.utils.ARG_CITY_NAME
import com.example.dailymeteo.utils.SEARCH_FAILURE
import com.example.dailymeteo.utils.SEARCH_NO_RESULTS
import com.example.dailymeteo.utils.SEARCH_UNSUCCESSFUL
import com.example.dailymeteo.view.main.MainFragment
import com.example.dailymeteo.viewmodel.SearchAppState
import com.example.dailymeteo.viewmodel.SearchViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.time.Duration

class SearchFragment: Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = Observer<SearchAppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        binding.searchInputLayout.setEndIconOnClickListener {
            val cityName = binding.searchEditText.text.toString()
            requestData(cityName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(appState: SearchAppState) = with(binding) {
        when(appState) {
            SearchAppState.Loading -> searchProgressBar.show()
            is SearchAppState.Success -> {
                searchProgressBar.hide()
                showData(appState.city)
            }
            is SearchAppState.Error -> {
                searchProgressBar.hide()
                showErrorMessage(appState.error)
            }
        }
    }

    private fun requestData(cityName: String) {
        viewModel.getGeocodingFromRemoteSource(cityName)
    }

    private fun showData(city: City) {
        val args = Bundle().apply {
            putParcelable(ARG_CITY_NAME, city)
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment.newInstance(args), "")
            .commit()
    }

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

    companion object {
        fun newInstance() = SearchFragment()
    }
}
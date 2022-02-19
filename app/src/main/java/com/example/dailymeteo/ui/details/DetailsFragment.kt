package com.example.dailymeteo.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import coil.load
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentDetailsBinding
import com.example.dailymeteo.domain.model.Current
import com.example.dailymeteo.utils.*

class DetailsFragment: Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var current: Current? = null
    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            current = it.getParcelable(ARG_CURRENT)
            cityName = it.getString(ARG_CITY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCurrent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showCurrent() = with(binding) {
        cityName?.let {
            cityTextView.text = it
        }
        current?.let {
            tempTextView.text = "${it.temp}"
            feelsTempTextView.text = "${it.feelsLike} ${getString(R.string.temp_unit)}"
            conditionImageView.load("$ICON_BASE_URL${it.icon}$ICON_LARGE$ICON_EXT")
            conditionTextView.text = it.description

            details.pressureValTextView.text = "${it.pressure} ${getString(R.string.pressure_unit)}"
            details.humidityValTextView.text = "${it.humidity} ${getString(R.string.humidity_unit)}"
            details.uviValTextView.text = "${it.uvIndex}"
            details.cloudinessValTextView.text = "${it.cloudiness} ${getString(R.string.percent)}"
            details.visibilityValTextView.text = "${it.visibility} ${getString(R.string.visibility_unit)}"

            wind.windDirImageView.rotation = it.windDir.toFloat()
            wind.windDirTextView.text = it.windDirName.name
            wind.windSpeedValTextView.text = "${String.format("%.1f", it.windSpeed)} ${getString(R.string.speed_unit)}"
            wind.windGustValTextView.text = "${String.format("%.1f", it.windGust)} ${getString(R.string.speed_unit)}"
        }
    }

    companion object {
        fun newInstance(current: Current, cityName: String): DetailsFragment {
            return DetailsFragment().apply {
                this.arguments = bundleOf(ARG_CURRENT to current, ARG_CITY to cityName)
            }
        }
    }
}
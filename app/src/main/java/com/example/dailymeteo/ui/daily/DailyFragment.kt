package com.example.dailymeteo.ui.daily

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentDailyBinding
import com.example.dailymeteo.domain.model.Daily
import com.example.dailymeteo.utils.*

class DailyFragment: Fragment() {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private var daily: List<Daily> = emptyList()
//    private var cityName: String? = null

    private val adapter by lazy { DailyRecyclerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            daily = it.getParcelableArrayList(ARG_DAILY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDaily()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showDaily() = with(binding) {
        dailyRecyclerView.adapter = adapter
        adapter.submitList(daily)
    }

    companion object {
        fun newInstance(daily: List<Daily>): DailyFragment {
            return DailyFragment().apply {
                this.arguments = bundleOf(ARG_DAILY to daily)
            }
        }
    }
}
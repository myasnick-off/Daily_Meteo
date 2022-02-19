package com.example.dailymeteo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dailymeteo.databinding.FragmentMainRecyclerItemBinding
import com.example.dailymeteo.domain.model.Daily
import com.example.dailymeteo.ui.search.SearchFragment
import com.example.dailymeteo.utils.ICON_BASE_URL
import com.example.dailymeteo.utils.ICON_EXT
import com.example.dailymeteo.utils.ICON_LARGE

class MainRecyclerAdapter :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    private var dailyWeather: MutableList<Daily> = mutableListOf()
    private lateinit var itemListener: MainFragment.ItemClickListener

    fun setItems(items: List<Daily>) {
        dailyWeather.clear()
        dailyWeather.addAll(items)
        notifyDataSetChanged()
    }

    fun setListener(listener: MainFragment.ItemClickListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            FragmentMainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dailyWeather[position])
    }

    override fun getItemCount() = dailyWeather.size

    inner class MainViewHolder(private val binding: FragmentMainRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily) = with(binding) {
            itemWeekDayTextView.text = daily.weekDay
            itemDateTextView.text = daily.dayMonth
            itemTempTextView.text = "${daily.temp.max}/${daily.temp.min}"
            itemConditionImageView.load("$ICON_BASE_URL${daily.icon}$ICON_LARGE$ICON_EXT")
            itemDescriptionTextView.text = daily.description
            root.setOnClickListener { itemListener.onItemClicked() }
        }
    }
}

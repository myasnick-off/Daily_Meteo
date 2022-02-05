package com.example.dailymeteo.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dailymeteo.databinding.FragmentMainItemBinding
import com.example.dailymeteo.domain.Daily
import com.example.dailymeteo.utils.ICON_BASE_URL
import com.example.dailymeteo.utils.ICON_EXT
import com.example.dailymeteo.utils.ICON_LARGE

class MainRecyclerAdapter: RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    private var dailyWeather: MutableList<Daily> = mutableListOf()

    fun setItems(items: List<Daily>) {
        dailyWeather.clear()
        dailyWeather.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            FragmentMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dailyWeather[position])
    }

    override fun getItemCount() = dailyWeather.size

    inner class MainViewHolder(view: View):RecyclerView.ViewHolder(view) {
        fun bind(daily: Daily) {
            FragmentMainItemBinding.bind(itemView).apply {
                itemWeekDayTextView.text = daily.weekDay
                itemDateTextView.text = daily.dayMonth
                itemTempTextView.text = "${daily.temp.max}/${daily.temp.min}"
                itemConditionImageView.load("$ICON_BASE_URL${daily.icon}$ICON_LARGE$ICON_EXT")
                itemDescriptionTextView.text = daily.description
            }
        }
    }
}
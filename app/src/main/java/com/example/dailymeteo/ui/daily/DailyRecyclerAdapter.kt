package com.example.dailymeteo.ui.daily

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dailymeteo.R
import com.example.dailymeteo.databinding.FragmentDailyItemBinding
import com.example.dailymeteo.databinding.FragmentMainRecyclerItemBinding
import com.example.dailymeteo.domain.model.Daily
import com.example.dailymeteo.utils.ICON_BASE_URL
import com.example.dailymeteo.utils.ICON_EXT
import com.example.dailymeteo.utils.ICON_LARGE

class DailyRecyclerAdapter: ListAdapter<Daily, DailyRecyclerAdapter.MainViewHolder>(GithubRepoItemCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            FragmentDailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MainViewHolder(private val binding: FragmentDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context = itemView.context
        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily) = with(binding) {
            itemWeekDayTextView.text = daily.weekDay
            itemDateTextView.text = daily.dayMonth
            itemConditionImageView.load("$ICON_BASE_URL${daily.icon}$ICON_LARGE$ICON_EXT")
            itemDescriptionTextView.text = daily.description

            morningItem.itemDaytimeTextView.text = context.getString(R.string.morning)
            dayItem.itemDaytimeTextView.text = context.getString(R.string.day)
            eveningItem.itemDaytimeTextView.text = context.getString(R.string.evening)
            nightItem.itemDaytimeTextView.text = context.getString(R.string.night)

            morningItem.itemTempTextView.text = "${daily.temp.morning} ${context.getString(R.string.temp_unit)}"
            dayItem.itemTempTextView.text = "${daily.temp.day} ${context.getString(R.string.temp_unit)}"
            eveningItem.itemTempTextView.text = "${daily.temp.evening} ${context.getString(R.string.temp_unit)}"
            nightItem.itemTempTextView.text = "${daily.temp.night} ${context.getString(R.string.temp_unit)}"

            detailsItem.pressureValTextView.text = "${daily.pressure} ${context.getString(R.string.pressure_unit)}"
            detailsItem.humidityValTextView.text = "${daily.humidity} ${context.getString(R.string.humidity_unit)}"
            detailsItem.uviValTextView.text = "${daily.uvIndex}"
            detailsItem.cloudinessValTextView.text = "${daily.cloudiness} ${context.getString(R.string.percent)}"
            detailsItem.precProbValTextView.text = "${daily.precProb} ${context.getString(R.string.percent)}"

            windItem.windDirImageView.rotation = daily.windDir.toFloat()
            windItem.windDirTextView.text = daily.windDirName.name
            windItem.windSpeedValTextView.text = "${String.format("%.1f", daily.windSpeed)} ${context.getString(R.string.speed_unit)}"
        }
    }
}

object GithubRepoItemCallBack : DiffUtil.ItemCallback<Daily>() {

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}
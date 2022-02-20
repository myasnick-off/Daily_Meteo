package com.example.dailymeteo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dailymeteo.databinding.FragmentMainRecyclerItemBinding
import com.example.dailymeteo.domain.model.Daily
import com.example.dailymeteo.utils.ICON_BASE_URL
import com.example.dailymeteo.utils.ICON_EXT
import com.example.dailymeteo.utils.ICON_LARGE

class MainRecyclerAdapter : ListAdapter<Daily, MainRecyclerAdapter.MainViewHolder>(DailyItemCallBack) {

    private lateinit var itemListener: MainFragment.ItemClickListener

    fun setListener(listener: MainFragment.ItemClickListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            FragmentMainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

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

object DailyItemCallBack : DiffUtil.ItemCallback<Daily>() {

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}

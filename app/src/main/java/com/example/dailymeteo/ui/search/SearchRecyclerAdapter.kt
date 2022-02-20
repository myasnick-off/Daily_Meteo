package com.example.dailymeteo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dailymeteo.databinding.FragmentSearchItemBinding
import com.example.dailymeteo.domain.model.City

class SearchRecyclerAdapter: ListAdapter<City, SearchRecyclerAdapter.CityViewHolder>(CityItemCallBack) {

    private lateinit var itemListener: SearchFragment.ItemClickListener

    fun getItems(): List<City> = currentList

    fun setListener(listener: SearchFragment.ItemClickListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CityViewHolder(private val binding: FragmentSearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) = with(binding) {
                itemCityName.text = city.name
                root.setOnClickListener { itemListener.onItemClicked(layoutPosition) }
        }
    }
}

object CityItemCallBack : DiffUtil.ItemCallback<City>() {

    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}
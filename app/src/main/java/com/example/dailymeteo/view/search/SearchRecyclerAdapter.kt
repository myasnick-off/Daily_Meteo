package com.example.dailymeteo.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailymeteo.databinding.FragmentSearchItemBinding
import com.example.dailymeteo.domain.City

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.CityViewHolder>() {

    private var cities: MutableList<City> = mutableListOf()
    private lateinit var itemListener: SearchFragment.ItemClickListener

    fun setItems(data: List<City>) {
        cities.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun getItems() = cities

    fun setListener(listener: SearchFragment.ItemClickListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount() = cities.size

    inner class CityViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(city: City) {
            FragmentSearchItemBinding.bind(itemView).apply {
                itemCityName.text = city.name
                itemView.setOnClickListener {
                    itemListener.onItemClicked(layoutPosition)
                }
            }
        }

    }
}
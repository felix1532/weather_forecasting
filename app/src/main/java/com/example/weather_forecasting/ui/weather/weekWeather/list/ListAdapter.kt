package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.weekWeather.General

class ListAdapter(private val list: ArrayList<General?>)
    : RecyclerView.Adapter<WeekWeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeekWeatherViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: WeekWeatherViewHolder, position: Int) {
        val forecastWeather: General? = list[position]
        holder.bind(forecastWeather , holder.itemView.context)
    }
    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)

    }

//    class SpecificViewHolder (inflater: LayoutInflater, parent: ViewGroup)
//        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_week_weather_fragment, parent, false)) {
//
//    }
}
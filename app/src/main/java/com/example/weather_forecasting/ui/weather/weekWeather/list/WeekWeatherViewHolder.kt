package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.weekWeather.General

class WeekWeatherViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_week_weather_fragment, parent, false)) {
    private var mDateView: TextView? = null
    private var mImageView: ImageView? = null
    private var mTemperatureView: TextView? = null
    private var mDescriptionView: TextView? = null

    init {
        mDateView = itemView.findViewById(R.id.time_text_recycler_view)
        mImageView = itemView.findViewById(R.id.image_recycler_view)
        mTemperatureView = itemView.findViewById(R.id.temperature_recycler_view)
        mDescriptionView = itemView.findViewById(R.id.description_text_recycler_view)

    }

    fun bind(forecastWeather: General?) {
        mDateView?.text = forecastWeather?.dtTxt
        if (forecastWeather != null) {
            mImageView?.setImageResource(forecastWeather.weather[0].id_drawable_icon)
        }
        mTemperatureView?.text = forecastWeather?.main?.temp?.toInt().toString() + " ℃"
        mDescriptionView?.text = forecastWeather?.weather?.get(0)?.description
    }
}
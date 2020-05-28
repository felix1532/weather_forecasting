package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.weekWeather.General
import kotlin.coroutines.coroutineContext

class WeekWeatherViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_week_weather_fragment, parent, false)) {
    private var mDateView: TextView? = null
    private var mImageView: ImageView? = null
    private var mTemperatureView: TextView? = null
    private var mDescriptionView: TextView? = null
    private  var relativeLayout:RelativeLayout? = null


    init {
        mDateView = itemView.findViewById(R.id.time_text_recycler_view)
        mImageView = itemView.findViewById(R.id.image_recycler_view)
        mTemperatureView = itemView.findViewById(R.id.temperature_recycler_view)
        mDescriptionView = itemView.findViewById(R.id.description_text_recycler_view)
        relativeLayout = itemView.findViewById(R.id.relativeLayout)


    }


    fun bind(forecastWeather: General?, context: Context) {
        relativeLayout?.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation))
        mImageView?.startAnimation(AnimationUtils.loadAnimation(context ,R.anim.fade_transition_animation))
        mDateView?.text = forecastWeather?.dtTxt
        if (forecastWeather != null) {
            mImageView?.setImageResource(forecastWeather.weather[0].id_drawable_icon)
        }
        mTemperatureView?.text = forecastWeather?.main?.temp?.toInt().toString() + " ℃"
        mDescriptionView?.text = forecastWeather?.weather?.get(0)?.description
    }
}
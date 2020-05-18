package com.example.weather_forecasting.ui.weather.weekWeather.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.weather_forecasting.R

class WeekWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            WeekWeatherFragment()
    }

    private lateinit var viewModel: WeekWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.week_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeekWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

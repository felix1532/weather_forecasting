package com.example.weather_forecasting.ui.weather.weekWeather.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.weather_forecasting.R

class DetailWeekWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            DetailWeekWeatherFragment()
    }

    private lateinit var viewModel: DetailWeekWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_week_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailWeekWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

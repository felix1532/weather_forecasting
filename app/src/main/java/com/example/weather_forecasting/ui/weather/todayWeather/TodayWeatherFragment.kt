package com.example.weather_forecasting.ui.weather.todayWeather

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.network.WeatherNetworkDataSourceImpl
import com.example.weather_forecasting.data.network.response.ConnectivityInterceptionImpl
import com.example.weather_forecasting.data.network.response.OpenWeatherApiService
import com.example.weather_forecasting.data.network.response.WeatherNetworkDataSource

import kotlinx.android.synthetic.main.today_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodayWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            TodayWeatherFragment()
    }

    private lateinit var viewModel: TodayWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.today_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodayWeatherViewModel::class.java)
        // TODO: Use the ViewModel

        val apiService =OpenWeatherApiService(ConnectivityInterceptionImpl(this.requireContext()))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)

        GlobalScope .launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchTodayWeather("London", "en","metric")
        }
        weatherNetworkDataSource.downloadedTodayWeather.observe(viewLifecycleOwner, Observer {
            temperature.text = it.toString()
        })


    }

}

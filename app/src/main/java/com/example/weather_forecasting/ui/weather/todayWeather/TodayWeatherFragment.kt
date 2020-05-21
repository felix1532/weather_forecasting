package com.example.weather_forecasting.ui.weather.todayWeather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.network.WeatherNetworkDataSourceImpl
import com.example.weather_forecasting.data.network.response.ConnectivityInterceptionImpl
import com.example.weather_forecasting.data.network.response.OpenWeatherApiService
import com.example.weather_forecasting.internal.GlideApp
import com.example.weather_forecasting.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.today_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition


class TodayWeatherFragment : ScopeFragment()  {


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



        val apiService = OpenWeatherApiService(ConnectivityInterceptionImpl(this.requireContext()))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
        GlobalScope .launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchTodayWeather("London", "en","metric")
        }
        weatherNetworkDataSource.downloadedTodayWeather.observe(viewLifecycleOwner, Observer {
            //temperature.text = it.toString()
            updateLocation("London")
            updateDateToToday()
            updateInfoWeatherFragment(it.main.temp,it.main.feelsLike,it.wind.speed,it.main.humidity,it.clouds.all)

            GlideApp.with(this@TodayWeatherFragment)
                .load("http:${it.base}")
                .into(imageView_condition_icon)
        })


    }



    private fun updateLocation(location:String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateInfoWeatherFragment(temperatures:Double, feelsLike:Double, winSpeed:Double, humidity:Int,clouds: Int){
        textView_temperature.text="$temperatures \u2103"
        textView_feels_like_temperature.text="Feels Like $feelsLike ℃"
        textView_wind.text="Wind $winSpeed m/s"
        textView_humidity.text="Humidity $humidity %"
        text_view_clouds.text="Clouds $clouds %"
    }



}


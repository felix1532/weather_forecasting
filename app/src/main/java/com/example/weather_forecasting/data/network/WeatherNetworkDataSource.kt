package com.example.weather_forecasting.data.network.response

import androidx.lifecycle.LiveData

interface WeatherNetworkDataSource {
    val downloadedTodayWeather :  LiveData<TodayWeatherResponse>

    suspend fun fetchTodayWeather(
        location: String,
        languageCode:String = "en",
        units:String = "metric"
    )

}
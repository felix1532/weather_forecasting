package com.example.weather_forecasting.data.network.response


import com.example.weather_forecasting.data.weekWeather.City
import com.example.weather_forecasting.data.weekWeather.General
import java.io.Serializable

data class ForecastWeatherResponse (
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<General>,
    val message: Int
) : Serializable
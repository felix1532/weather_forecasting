package com.example.weather_forecasting.model.network.response


import com.example.weather_forecasting.model.weekWeather.City
import com.example.weather_forecasting.model.weekWeather.General
import java.io.Serializable

data class ForecastWeatherResponse (
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<General>,
    val message: Int
) : Serializable
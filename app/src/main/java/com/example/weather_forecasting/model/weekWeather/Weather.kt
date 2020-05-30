package com.example.weather_forecasting.model.weekWeather


data class Weather(
    var description: String,
    val icon: String,
    var id: Int,
    val main: String,
    var id_drawable_icon: Int
)
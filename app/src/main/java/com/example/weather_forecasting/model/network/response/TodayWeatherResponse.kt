package com.example.weather_forecasting.model.network.response

import com.example.weather_forecasting.model.modelTodayWeather.entity.*
import java.io.Serializable


data class TodayWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) :Serializable
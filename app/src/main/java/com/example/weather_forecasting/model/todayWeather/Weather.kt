package com.example.weather_forecasting.model.todayWeather.entity


import com.google.gson.annotations.SerializedName

data class Weather(
    val description: String,
    @SerializedName("icon")
    val icon: String,
    val id: Int,
    val main: String

)
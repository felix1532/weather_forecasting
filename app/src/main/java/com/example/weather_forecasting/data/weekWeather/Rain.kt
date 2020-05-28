package com.example.weather_forecasting.data.weekWeather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)
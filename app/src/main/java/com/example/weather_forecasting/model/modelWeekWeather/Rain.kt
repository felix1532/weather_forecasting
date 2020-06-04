package com.example.weather_forecasting.model.modelWeekWeather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)
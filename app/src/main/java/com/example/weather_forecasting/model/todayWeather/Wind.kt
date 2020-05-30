package com.example.weather_forecasting.model.todayWeather.entity


import com.google.gson.annotations.SerializedName



data class Wind(

    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)

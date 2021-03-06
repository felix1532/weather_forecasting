package com.example.weather_forecasting.model.modelWeekWeather


import com.google.gson.annotations.SerializedName

data class General(
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val rain: Rain,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind,
    var timeDayMonthYear:String,
    var timeHoursMinutes:String
)
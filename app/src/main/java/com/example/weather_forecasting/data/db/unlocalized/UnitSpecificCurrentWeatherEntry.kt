package com.example.weather_forecasting.data.db.unlocalized

import androidx.room.Entity
import com.example.weather_forecasting.data.db.entity.Main


interface UnitSpecificCurrentWeatherEntry
{
    val id: Int
    val deg: Int
    val speed: Double

   // val cloudsAll: Int
}
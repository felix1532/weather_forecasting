package com.example.weather_forecasting.data.db.unlocalized

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class MetricTodayWeatherEntry(

    @ColumnInfo(name = "id")
    override val id: Int,
    @ColumnInfo(name = "deg")
    override val deg: Int,
    @ColumnInfo(name = "speed")
    override val speed: Double

    //@ColumnInfo(name = "clouds_")
    //override val clouds: Int,



):UnitSpecificCurrentWeatherEntry
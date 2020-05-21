package com.example.weather_forecasting.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName



data class Wind(

    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)

package com.example.weather_forecasting.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "today_weather")
data class Wind(

    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID

}
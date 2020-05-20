package com.example.weather_forecasting.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather_forecasting.data.db.entity.CURRENT_WEATHER_ID
import com.example.weather_forecasting.data.db.unlocalized.MetricTodayWeatherEntry
import com.example.weather_forecasting.data.db.unlocalized.UnitSpecificCurrentWeatherEntry


@Dao
interface TodayWeatherDao
{

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun upset(weatherEntry: UnitSpecificCurrentWeatherEntry)


     @Query("select * from today_weather where id = $CURRENT_WEATHER_ID")
     fun getWeatherMetric():LiveData<MetricTodayWeatherEntry>

}
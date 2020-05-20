package com.example.weather_forecasting.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather_forecasting.data.db.entity.Wind
import org.kodein.di.Volatile


@Database(
    entities = [Wind::class],
    version = 1,
    exportSchema = false
)
abstract class ForecastDatabase : RoomDatabase()
{
    abstract fun todayWeatherDao(): TodayWeatherDao

    companion object{
        @Volatile private var instance : ForecastDatabase ?=null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance =it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, ForecastDatabase::class.java, "weather.db")
                .build()


    }
}
package com.example.weather_forecasting.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather_forecasting.data.network.response.OpenWeatherApiService
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import com.example.weather_forecasting.data.network.response.WeatherNetworkDataSource
import com.example.weather_forecasting.internal.NoConnectivityExcretion

class WeatherNetworkDataSourceImpl(
    private val OpenWeatherApiService : OpenWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadTodayWeather = MutableLiveData<TodayWeatherResponse>()
    override val downloadedTodayWeather: LiveData<TodayWeatherResponse>
        get() = _downloadTodayWeather

    override suspend fun fetchTodayWeather(location: String, languageCode: String, units: String) {
        try {
            val fetchedTodayWeather = OpenWeatherApiService
                .getTodayWeather(location,languageCode,units)
                .await()
            _downloadTodayWeather.postValue(fetchedTodayWeather)
        }
        catch (e:NoConnectivityExcretion){
            Log.e("Connectivity","No internet connection", e)
        }
    }
}
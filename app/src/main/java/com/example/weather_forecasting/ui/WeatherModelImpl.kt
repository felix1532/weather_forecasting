package com.example.weather_forecasting.ui

import android.content.Context
import android.widget.Toast
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import com.example.weather_forecasting.data.network.response.ForecastWeatherApiClient
import com.example.weather_forecasting.data.network.response.ForecastWeatherService
import io.reactivex.Observable
import java.util.*

class WeatherModelImpl (context: Context) : WeatherContract.Model {

    var context: Context = context


    private val weatherRestService: ForecastWeatherService =
        ForecastWeatherApiClient.getClient().create(ForecastWeatherService::class.java)


    override fun initiateWeatherInfoCall(latitude: Double, longitude:Double ): Observable<TodayWeatherResponse> {
        return weatherRestService.getWeatherInfo( setLanguageSystem().toString(),latitude, longitude,"metric")
    }

    override fun fetchInvalidCityMessage() {
        Toast.makeText(context.applicationContext,"Неверный координаты",Toast.LENGTH_LONG).show()

    }




    override fun setLanguageSystem(): String? {
        var languageCode:String
        if(Locale.getDefault().language.equals("ru")){
            languageCode = "ru"
        }else languageCode="en"
        return languageCode
    }


}
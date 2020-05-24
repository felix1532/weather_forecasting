package com.example.weather_forecasting.ui

import android.content.Context
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import com.example.weather_forecasting.data.network.response.ForecastWeatherApiClient
import com.example.weather_forecasting.data.network.response.ForecastWeatherService
import io.reactivex.Observable
import java.util.*

class WeatherModelImpl (context: Context) : WeatherContract.Model {

    var context: Context = context

    private val weatherRestService: ForecastWeatherService =
        ForecastWeatherApiClient.getClient().create(ForecastWeatherService::class.java)


    override fun initiateWeatherInfoCall(textToBeSearched: String): Observable<TodayWeatherResponse> {
        return weatherRestService.getWeatherInfo(textToBeSearched, setLanguageSystem().toString(),"metric")
    }

    override fun fetchInvalidCityMessage(): String {
        return context.getString(R.string.invalid_city)
    }



    override fun setLanguageSystem(): String? {
        var languageCode:String
        if(Locale.getDefault().language.equals("ru")){
            languageCode = "ru"
        }else languageCode="en"
        return languageCode
    }

}
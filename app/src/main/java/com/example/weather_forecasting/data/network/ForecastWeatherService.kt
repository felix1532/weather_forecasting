package com.example.weather_forecasting.data.network.response

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastWeatherService {

    // https://api.openweathermap.org/data/2.5/weather?q=London&lang=en&units=metric&APPID=f393977e883c4910e9c68c1588f4ea5f

    @GET("data/2.5/weather?$API_KEY")
    fun getWeatherInfo(@Query (value = "lang") language: String,
                       @Query(value = "lat") latitude:Double ,
                       @Query(value = "lon") longitude:Double ,
                       @Query(value = "units") units:String = "metric"
    ): Observable<TodayWeatherResponse>



    @GET("data/2.5/forecast?$API_KEY")
    fun getForecastWeatherInfo(@Query (value = "lang") language: String,
                       @Query(value = "lat") latitude:Double ,
                       @Query(value = "lon") longitude:Double ,
                       @Query(value = "units") units:String = "metric"
    ): Observable<ForecastWeatherResponse>
}
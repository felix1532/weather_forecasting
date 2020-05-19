package com.example.weather_forecasting.data

import com.example.weather_forecasting.data.response.Main
import com.example.weather_forecasting.data.response.TodayWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "f393977e883c4910e9c68c1588f4ea5f"

// https://api.openweathermap.org/data/2.5/weather?q=London&lang=en&units=metric&APPID=f393977e883c4910e9c68c1588f4ea5f

interface OpenWeatherApiService
{
    @GET(value="weather")
    fun getTodayWeather(
        @Query (value = "q") location: String,
        @Query(value = "lang") languageCode:String = "en",
        @Query(value = "units") units:String = "metric"
    ):Deferred <TodayWeatherResponse>

    companion object{
        operator fun invoke(): OpenWeatherApiService{
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("APPID", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApiService::class.java)
        }
    }
}
package com.example.weather_forecasting.model.network.response

import com.example.weather_forecasting.model.network.DEFAULT_CONNECT_TIMEOUT_IN_MS
import com.example.weather_forecasting.model.network.DEFAULT_READ_TIMEOUT_IN_MS
import com.example.weather_forecasting.model.network.DEFAULT_WRITE_TIMEOUT_IN_MS
import com.example.weather_forecasting.model.network.END_POINT
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ForecastWeatherApiClient
{
    lateinit var retrofit: Retrofit

    fun getClient(): Retrofit {
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val oktHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
        oktHttpClient.addInterceptor(logging)
        oktHttpClient.addInterceptor { chain ->
            var original: Request = chain.request()
            var request: Request = original.newBuilder()
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        if (!ForecastWeatherApiClient::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(END_POINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build()
        }
        return retrofit
    }

}
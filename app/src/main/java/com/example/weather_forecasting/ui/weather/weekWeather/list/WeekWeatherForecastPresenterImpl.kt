package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.content.Context
import android.location.Location
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.network.response.ForecastWeatherResponse
import com.example.weather_forecasting.data.weekWeather.General
import com.example.weather_forecasting.ui.WeatherContract
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeekWeatherForecastPresenterImpl (

    viewWeek: WeatherContract.WeekView,
    model: WeatherContract.Model,
    processThread: Scheduler,
    mainThread: Scheduler,
    context: Context
): WeatherContract.PresenterWeekWeather {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var viewWeek: WeatherContract.WeekView = viewWeek
    var model: WeatherContract.Model = model
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread
    var context:Context = context


    override fun getGeolocation( ) {
        var mLocation: Location? = null
        var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        getForecastWeatherData(location.latitude,location.longitude)

                    }
                }

    }


    override fun getForecastWeatherData(latitide:Double,longitude:Double) {
        if (latitide!=0.0 && longitude!=0.0) {
            viewWeek.handleLoaderView(true)
            viewWeek.handleWeatherView(false)
            viewWeek.handleErrorView(false)



            compositeDisposable.add(
                model.forecastWeatherInfoCall(latitide, longitude).subscribeOn(processThread).observeOn(
                    mainThread
                ).subscribeWith(object : DisposableObserver<ForecastWeatherResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(forecastWeatherResponse: ForecastWeatherResponse) {
                        handleForecastInfoResponse(forecastWeatherResponse)
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            try {
                                val body = e.response().errorBody()!!.string()
                                handleForecastInfoResponse(
                                    Gson().fromJson(
                                        body, ForecastWeatherResponse::class.java
                                    )
                                )
                            } catch (e1: IOException) {
                                e1.printStackTrace()
                            }

                        } else {
                            viewWeek.handleErrorView(true)
                        }
                    }

                })
            )
        } else {
            viewWeek.showErrorMessage(model.fetchInvalidCord());
        }
    }


    override fun handleForecastInfoResponse(forecastWeatherResponse: ForecastWeatherResponse?) {
        val weekForecastingWeather: ArrayList<General?> = ArrayList()
        for (i in 0..forecastWeatherResponse?.list?.size!!-1)
        {
            forecastWeatherResponse.list[i].weather[0].id_drawable_icon = getImageForCode(forecastWeatherResponse.list[i].weather[0].id)
            weekForecastingWeather.add(forecastWeatherResponse.list[i])
        }

        viewWeek.handleLoaderView(false)
        viewWeek.handleWeatherView(true)
        viewWeek.handleErrorView(false)
        viewWeek.infoForecastDaysForWeekFragment(weekForecastingWeather)
    }


    override fun firstLetterUppercase(string:String) : String {
        var stringFLUppercase = ""
        stringFLUppercase += string.substring(0, 1).toUpperCase()
        for (i in 1 until string.length) {
            stringFLUppercase +=string.substring(i,i+1)
        }
        return stringFLUppercase
    }


    override fun getImageForCode(code: Int?): Int = when (code) {
        200,230-> {
            R.drawable.clouds_with_lighting_littlerain_01
        }
        201,202-> {
            R.drawable.clouds_with_lighting_rain_01
        }
        210,211-> {
            R.drawable.clouds_with_lighting_01
        }
        212,221 -> {
            R.drawable.clouds_with_2lighting_01
        }
        231,232 -> {
            R.drawable.clouds_with_lighting_rain_01
        }
        300,301,302,310,311 -> {
            R.drawable.clouds_with_littlerain_01
        }
        312,313,314,321 -> {
            R.drawable.clouds_with_rain_01
        }
        500,501 -> {
            R.drawable.clouds_with_littlerain_01
        }
        502,503,504,511,520,521,522,531 -> {
            R.drawable.clouds_with_rain_01
        }
        600,601,602,611,612 -> {
            R.drawable.clouds_with_littlesnow_01
        }
        613,615,616,620,621,622 -> {
            R.drawable.clouds_with_snow_01
        }
        701,711,721,731,741,751,761,762,771,781,782,783,784,785,786,787,788,789,790,791,792,793,
        794,795,796,797,798,799-> {
            R.drawable.tornado
        }
        800 -> {
            R.drawable.clouds_01
        }
        801,802-> {
            R.drawable.sun_with_3clouds_01
        }
        803,804-> {
            R.drawable.clouds_01
        }

        else -> {
            R.drawable.sun_01
        }
    }

    override fun formatSunriseSunsetDate(long: Long?): String? {
        return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(long?.times(1000)?.let { Date(it) }).toString()
    }

    override fun formatDateForForecastingWeather(long: Long?): String? {
        val time = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        time.timeZone = TimeZone.getTimeZone("GMT")
        return time.format(long?.times(1000)?.let { Date(it) }).toString()
    }


    private fun getDateTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        val date = Date()
        return dateFormat.format(date).toString()
    }



    override fun destroyView() {
        compositeDisposable.dispose()
    }


}
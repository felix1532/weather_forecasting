package com.example.weather_forecasting.ui

import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import com.google.gson.Gson
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import org.apache.commons.lang3.StringUtils
import retrofit2.HttpException
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastPresenterImpl (
    view: WeatherContract.View, model: WeatherContract.Model,
    processThread: Scheduler, mainThread: Scheduler
): WeatherContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var view: WeatherContract.View = view
    var model: WeatherContract.Model = model
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread

    override fun init() {
        view.onInitView()
    }


    override fun getWeatherData(textToBeSearched: String) {
        if (!StringUtils.isBlank(textToBeSearched)) {
            view.handleLoaderView(true)
            view.handleWeatherView(false)
            view.handleErrorView(false)

            compositeDisposable.add(
                model.initiateWeatherInfoCall(textToBeSearched).subscribeOn(processThread).observeOn(
                    mainThread
                ).subscribeWith(object : DisposableObserver<TodayWeatherResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(todayWeatherResponse: TodayWeatherResponse) {
                        handleInfoResponse(todayWeatherResponse)
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            try {
                                val body = e.response().errorBody()!!.string()
                                handleInfoResponse(
                                    Gson().fromJson(
                                        body, TodayWeatherResponse::class.java
                                    )
                                )
                            } catch (e1: IOException) {
                                e1.printStackTrace()
                            }

                        } else {
                            view.handleErrorView(true)
                        }
                    }

                })
            )
        } else {
            view.showErrorMessage(model.fetchInvalidCityMessage());
        }
    }


    override fun handleInfoResponse(todayWeatherResponse: TodayWeatherResponse?) {

        val cityName = todayWeatherResponse?.name
        val description = todayWeatherResponse?.weather?.get(0)?.description
        val temperatures = todayWeatherResponse?.main?.temp
        val sunset = todayWeatherResponse?.sys?.sunset?.toLong()
        val sunrise = todayWeatherResponse?.sys?.sunrise?.toLong()
        val winSpeed = todayWeatherResponse?.wind?.speed
        val humidity = todayWeatherResponse?.main?.humidity
        val clouds = todayWeatherResponse?.clouds?.all
        val id = todayWeatherResponse?.weather?.get(0)?.id
        val pressure = todayWeatherResponse?.main?.pressure





        view.handleLoaderView(false)
        view.handleWeatherView(true)
        view.handleErrorView(false)
        if (temperatures != null && sunset != null) {
            getDateTime()?.let {
                view.setInfoCurrentDay(cityName,temperatures,
                    description?.let { firstLetterUppercase(it) },
                    formatSunriseSunsetDate(sunset),
                    formatSunriseSunsetDate(sunrise),
                    humidity ,
                    clouds,
                    winSpeed,
                    getImageForCode(id),
                    pressure ,
                    firstLetterUppercase(it)
                )
            }

        }

//        val forecastData = ArrayList<ForecastDataModel>()
//        if (cityName != null && description != null && temperatures != null && feelsLike != null && winSpeed != null && humidity != null && clouds != null)
//            forecastData.add(
//                ForecastDataModel(
//                    cityName,
//                    firstLetterUppercase(description),
//                    temperatures,
//                    feelsLike,
//                    winSpeed,
//                    humidity,
//                    clouds
//                )
//            )

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

    private fun getDateTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        val date = Date()
        return dateFormat.format(date).toString()
    }




    override fun destroyView() {
        compositeDisposable.dispose()
    }


}
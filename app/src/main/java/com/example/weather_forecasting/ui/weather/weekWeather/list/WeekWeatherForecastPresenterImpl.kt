package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.network.response.ForecastWeatherResponse
import com.example.weather_forecasting.model.weekWeather.General
import com.example.weather_forecasting.ui.WeatherContract
import com.example.weather_forecasting.ui.WeatherModelImpl
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
    processThread: Scheduler,
    mainThread: Scheduler,
    context: Context
): WeatherContract.PresenterWeekWeather {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var viewWeek: WeatherContract.WeekView = viewWeek
    var model: WeatherContract.Model =  WeatherModelImpl(context)
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread
    var context:Context = context


    override fun getDateFromGeolocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewWeek.showErrorMessage(context.resources.getString(R.string.enable_location))
            viewWeek.handleErrorView(true)
            viewWeek.handleLoaderView(false)
            viewWeek.handleWeatherView(false)
            viewWeek.showButtonEnableGeolocation(true)
        }
        else {
            var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        getForecastWeatherData(location.latitude, location.longitude)
                    }
                }
        }

    }

    override fun getForecastWeatherData(latitide:Double,longitude:Double) {
        if (latitide!=0.0 && longitude!=0.0) {
            viewWeek.handleLoaderView(true)
            viewWeek.handleWeatherView(false)
            viewWeek.handleErrorView(false)
            viewWeek.showButtonEnableGeolocation(false)

            if(isInternetAvailable(context))
            {
                compositeDisposable.add(
                    model.forecastWeatherInfoCall(latitide, longitude).subscribeOn(processThread).observeOn(
                        mainThread
                    ).subscribeWith(object : DisposableObserver<ForecastWeatherResponse>() {
                        override fun onComplete() {
                        }

                        override fun onNext(weekWeatherResponse: ForecastWeatherResponse) {
                            handleForecastInfoResponse(weekWeatherResponse)
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

            }else{
                viewWeek.handleLoaderView(false)
                viewWeek.handleWeatherView(false)
                viewWeek.handleErrorView(true)
                viewWeek.showButtonEnableGeolocation(false)
                Toast.makeText(context,context.resources.getString(R.string.turn_internet), Toast.LENGTH_LONG).show()
            }

        } else {
            viewWeek.showErrorMessage(model.fetchInvalidCord());
            viewWeek.handleErrorView(true)
        }
    }


    override fun handleForecastInfoResponse(forecastWeatherResponse: ForecastWeatherResponse?) {
        val weekForecastingWeather: ArrayList<General?> = ArrayList()
        var count:Int =0
        val namekDays: MutableMap<Int, String> = mutableMapOf()

        weekForecastingWeather.add(forecastWeatherResponse?.list?.get(0))
        for (i in 1..forecastWeatherResponse?.list?.size!!-1 )
        {
            forecastWeatherResponse.list[i].timeHoursMinutes = formatHoursMinutes(forecastWeatherResponse.list[i].dt.toLong())
            forecastWeatherResponse.list[i].timeDayMonthYear = formatDateDayMonthYear(forecastWeatherResponse.list[i].dt.toLong())
            forecastWeatherResponse.list[i].weather[0].description = firstLetterUppercase(forecastWeatherResponse.list[i].weather[0].description)
            forecastWeatherResponse.list[i].weather[0].id_drawable_icon = getImageForCode(forecastWeatherResponse.list[i].weather[0].id)
            weekForecastingWeather.add(forecastWeatherResponse.list[i])
            if(weekForecastingWeather[i+count]?.timeHoursMinutes == "00:00" && i+count-1 != forecastWeatherResponse.list.size-1){
                weekForecastingWeather.add(forecastWeatherResponse.list[i])
                count+=1
            }
        }

        var flag:Boolean = false
        namekDays.put(0, context.resources.getString(R.string.todayWeatherForecasting))
        for (i in 1..weekForecastingWeather?.size-1)
        {
            val todayDate : String = getCurrentDate()
            if(weekForecastingWeather[i]?.timeDayMonthYear != todayDate &&
                weekForecastingWeather[i]?.timeHoursMinutes == "00:00"&&
                weekForecastingWeather[i-1]?.timeHoursMinutes == "00:00" &&
                i !=weekForecastingWeather?.size-1)
            {
                if(!flag)
                {
                    namekDays.put(i, context.resources.getString(R.string.tomorrow))
                    flag = true
                }else
                {
                    namekDays.put(i, firstLetterUppercase(getNameDayWeek(weekForecastingWeather[i]?.dt?.toLong())))
                }
            }
        }

        viewWeek.handleLoaderView(false)
        viewWeek.handleWeatherView(true)
        viewWeek.handleErrorView(false)
        viewWeek.showButtonEnableGeolocation(false)
        viewWeek.infoForecastDaysForWeekFragment(weekForecastingWeather, namekDays)
    }

    override fun firstLetterUppercase(string: String?): String {
        var stringFLUppercase = ""
        stringFLUppercase += string?.substring(0, 1)?.toUpperCase()
        for (i in 1 until string!!.length) {
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

    override fun formatHoursMinutes(long: Long?): String {
        val time = SimpleDateFormat("HH:mm")
        time.timeZone = TimeZone.getTimeZone("GMT")
        return time.format(long?.times(1000)?.let { Date(it) }).toString()
    }

    override fun formatDateDayMonthYear(long: Long?): String {
        val time = SimpleDateFormat("yyyy.MM.dd")
        time.timeZone = TimeZone.getTimeZone("GMT")
        return time.format(long?.times(1000)?.let { Date(it) }).toString()
    }

    override fun formatDateForForecastingWeather(long: Long?): String? {
        val time = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        time.timeZone = TimeZone.getTimeZone("GMT")
        return time.format(long?.times(1000)?.let { Date(it) }).toString()
    }

    override fun getCurrentDate():String{
        val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")
        val date = Date()
        return dateFormat.format(date).toString()
    }

    override fun getCurrentTime():String {
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        return dateFormat.format(date).toString()
    }

    override fun getNameDayWeek(time:Long?):String {
        val dateFormat = SimpleDateFormat("EEEE")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(time?.times(1000)?.let { Date(it) }).toString()
    }

    override fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

    override fun destroyView() {
        compositeDisposable.dispose()
    }


}
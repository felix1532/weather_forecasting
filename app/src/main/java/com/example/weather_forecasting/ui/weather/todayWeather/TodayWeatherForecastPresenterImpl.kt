package com.example.weather_forecasting.ui.weather.todayWeather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.network.response.TodayWeatherResponse
import com.example.weather_forecasting.model.todayWeather.entity.Coord
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
import java.lang.ref.WeakReference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TodayWeatherForecastPresenterImpl(
    viewToday: WeatherContract.TodayView,
    processThread: Scheduler,
    mainThread: Scheduler,
    context: Context
): WeatherContract.PresenterTodayWeather {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var viewToday: WeatherContract.TodayView = viewToday
    var model: WeatherContract.Model = WeatherModelImpl(context)
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread
    var context:Context = context

    override fun getDateFromGeolocation( ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewToday.showErrorMessage(context.resources.getString(R.string.enable_location))
            viewToday.handleErrorView(true)
            viewToday.handleLoaderView(false)
            viewToday.handleWeatherView(false)
            viewToday.showButtonEnableGeolocation(true)
        }
        else
        {
            var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getTodayWeatherData(location.latitude, location.longitude)
                }
            }
        }

    }

    override fun getTodayWeatherData(latitude:Double,longitude:Double) {
            if (latitude != 0.0 && longitude != 0.0 ) {
                viewToday.handleLoaderView(true)
                viewToday.handleWeatherView(false)
                viewToday.handleErrorView(false)
                viewToday.showButtonEnableGeolocation(false)

                if (isInternetAvailable(context)) {
                    compositeDisposable.add(
                        model.todayWeatherInfoCall(latitude, longitude).subscribeOn(processThread)
                            .observeOn(
                                mainThread
                            ).subscribeWith(object : DisposableObserver<TodayWeatherResponse>() {
                            override fun onComplete() {
                            }

                            override fun onNext(todayWeatherResponse: TodayWeatherResponse) {
                                handleTodayInfoResponse(todayWeatherResponse)
                            }

                            override fun onError(e: Throwable) {
                                if (e is HttpException) {
                                    try {
                                        val body = e.response().errorBody()!!.string()
                                        handleTodayInfoResponse(
                                            Gson().fromJson(
                                                body, TodayWeatherResponse::class.java
                                            )
                                        )
                                    } catch (e1: IOException) {
                                        e1.printStackTrace()
                                    }

                                } else {
                                    viewToday.handleErrorView(true)
                                }
                            }

                        })
                    )

                } else {
                    viewToday.handleLoaderView(false)
                    viewToday.handleWeatherView(false)
                    viewToday.handleErrorView(true)
                    viewToday.showButtonEnableGeolocation(false)
                    Toast.makeText(context, context.resources.getString(R.string.turn_internet), Toast.LENGTH_LONG).show()
                }

            } else {
                viewToday.handleErrorView(true)
                viewToday.showErrorMessage(model.fetchInvalidCord())
            }
        }


    override fun handleTodayInfoResponse(todayWeatherResponse: TodayWeatherResponse?) {

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

        viewToday.handleLoaderView(false)
        viewToday.handleWeatherView(true)
        viewToday.handleErrorView(false)
        viewToday.showButtonEnableGeolocation(false
        )
        if (temperatures != null && sunset != null) {
            getDateTime()?.let {
                viewToday.setInfoCurrentDay(cityName,temperatures,
                    description?.let { firstLetterUppercase(it) },
                    formatHoursMinutes(sunset),
                    formatHoursMinutes(sunrise),
                    humidity ,
                    clouds,
                    winSpeed,
                    getImageForCode(id),
                    pressure ,
                    firstLetterUppercase(it)
                )
            }

        }
    }

    override fun firstLetterUppercase(string: String?): String {
        var stringFLUppercase = ""
        stringFLUppercase += string?.substring(0, 1)?.toUpperCase()
        for (i in 1 until string!!.length) {
            stringFLUppercase += string.substring(i,i+1)
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
        return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(long?.times(1000)?.let { Date(it) })
    }

    override fun formatDateDayMonthYear(long: Long?): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(long?.times(1000)?.let { Date(it) })
    }

    override fun formatDateForForecastingWeather(long: Long?): String? {
        val time = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        time.timeZone = TimeZone.getTimeZone("GMT")
        return time.format(long?.times(1000)?.let { Date(it) }).toString()
    }

    override fun getDateTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")
        val date = Date()
        return dateFormat.format(date).toString()
    }


    override fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
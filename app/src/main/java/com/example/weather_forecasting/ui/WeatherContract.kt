package com.example.weather_forecasting.ui

import android.content.Context
import com.example.weather_forecasting.model.network.response.ForecastWeatherResponse
import com.example.weather_forecasting.model.modelWeekWeather.General
import com.example.weather_forecasting.model.network.response.TodayWeatherResponse
import io.reactivex.Observable

interface WeatherContract
{

    interface View {
          fun showErrorMessage(message: String)
          fun handleLoaderView(showHandleLoader: Boolean)
          fun handleWeatherView(showWeatherView: Boolean)
          fun handleErrorView(showErrorView: Boolean)
          fun showButtonEnableGeolocation(showButton:Boolean)
    }

    interface WeekView : View {
        fun setInfoWeekDays (weekForecastingWeather: ArrayList<General?>, nameDays: MutableMap<Int, String>)
    }

    interface TodayView : View{
        fun setInfoCurrentDay(
            cityName: String?,
            temperature: Double?,
            description:String?,
            sunset: String?,
            sunrise:String?,
            humidity: Int?,
            clouds: Int?,
            winSpeed:Double?,
            id: Int?,
            pressure:Int?,
            todayDate:String?
        )
        fun checkPermissionGeolocation () :Boolean
    }

    interface Presenter {
        fun firstLetterUppercase (string: String?) : String
        fun getImageForCode(code: Int?) :Int
        fun formatHoursMinutes(long: Long?) :String?
        fun formatDateDayMonthYear(long: Long?): String?
        fun getDateFromGeolocation()
        fun formatDateForForecastingWeatherUpdate(long: Long?): String?
        fun isInternetAvailable(context: Context?): Boolean
    }

    interface PresenterTodayWeather :Presenter{
        fun getTodayWeatherData(latitude:Double,longitude:Double)
        fun handleTodayInfoResponse(todayWeatherResponse: TodayWeatherResponse?)
        fun getDateTime(): String?
        fun getStringFroShareButton()

    }

    interface PresenterWeekWeather :Presenter{
        fun getForecastWeatherData(latitude:Double,longitude:Double)
        fun handleForecastInfoResponse(forecastWeatherResponse: ForecastWeatherResponse?)
        fun getCurrentDate():String
        fun getCurrentTime():String
        fun getNameDayWeek(time:Long?):String
    }

    interface Model {
        fun fetchInvalidCord() : String
        fun todayWeatherInfoCall(latitude:Double,longitude:Double): Observable<TodayWeatherResponse>
        fun forecastWeatherInfoCall(latitude:Double,longitude:Double): Observable<ForecastWeatherResponse>
        fun setLanguageSystem(): String?
    }
}
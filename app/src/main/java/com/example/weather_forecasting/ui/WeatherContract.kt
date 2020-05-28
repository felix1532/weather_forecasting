package com.example.weather_forecasting.ui

import com.example.weather_forecasting.data.network.response.ForecastWeatherResponse
import com.example.weather_forecasting.data.weekWeather.General
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import io.reactivex.Observable

interface WeatherContract
{

    interface View {

          fun showErrorMessage(invalidCityMessage: Unit)
          fun handleLoaderView(showHandleLoader: Boolean)
          fun handleWeatherView(showWeatherView: Boolean)
          fun handleErrorView(showErrorView: Boolean)
    }

    interface WeekView : View {
        fun infoForecastDaysForWeekFragment (weekForecastingWeather: ArrayList<General?>)
    }

    interface TodayView : View{
        fun setInfoCurrentDay(
            cityName: String?,
            temperature: Double,
            description:String?,
            sunset: String?,
            sunrise:String?,
            humidity: Int?,
            clouds: Int?,
            winSpeed:Double?,
            id: Int?,
            pressure:Int?,
            todayDate:String
        )
    }



    interface Presenter {
        fun destroyView()
        fun firstLetterUppercase (string: String) : String
        fun getImageForCode(code: Int?) :Int
        fun formatSunriseSunsetDate(long: Long?) :String?
        fun getGeolocation()
        fun formatDateForForecastingWeather(long: Long?): String?
    }

    interface PresenterTodayWeather :Presenter{
        fun getTodayWeatherData(latitude:Double,longitude:Double)
        fun handleTodayInfoResponse(todayWeatherResponse: TodayWeatherResponse?)
    }

    interface PresenterWeekWeather :Presenter{
        fun getForecastWeatherData(latitude:Double,longitude:Double)
        fun handleForecastInfoResponse(forecastWeatherResponse: ForecastWeatherResponse?)
    }



    interface Model {
        fun fetchInvalidCord()
        fun todayWeatherInfoCall(latitude:Double,longitude:Double): Observable<TodayWeatherResponse>
        fun forecastWeatherInfoCall(latitude:Double,longitude:Double): Observable<ForecastWeatherResponse>
        fun setLanguageSystem(): String?
    }
}
package com.example.weather_forecasting.ui

import android.app.Activity
import com.example.weather_forecasting.data.db.entity.ForecastDataModel
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import io.reactivex.Observable
import java.util.ArrayList

interface WeatherContract
{

    interface View {
          fun onInitView()
          fun showErrorMessage(invalidCityMessage: Unit)
          fun handleLoaderView(showHandleLoader: Boolean)
          fun handleWeatherView(showWeatherView: Boolean)
          fun handleErrorView(showErrorView: Boolean)
          fun showForeCastData(forecastData: ArrayList<ForecastDataModel>)
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
        fun init()
        fun getWeatherData(latitide:Double,longitude:Double)
        fun destroyView()
        fun handleInfoResponse(todayWeatherResponse: TodayWeatherResponse?)
        fun firstLetterUppercase (string: String) : String
        fun getImageForCode(code: Int?) :Int
        fun formatSunriseSunsetDate(long: Long?) :String?
        fun  getForecastTodayByGeolocation()
    }

    interface Model {
        fun fetchInvalidCityMessage()
        fun initiateWeatherInfoCall(latitude:Double,longitude:Double): Observable<TodayWeatherResponse>
        fun setLanguageSystem(): String?
    }
}
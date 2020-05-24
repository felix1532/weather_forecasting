package com.example.weather_forecasting.ui

import com.example.weather_forecasting.data.db.entity.ForecastDataModel
import com.example.weather_forecasting.data.network.response.TodayWeatherResponse
import io.reactivex.Observable
import java.util.ArrayList

interface WeatherContract
{

    interface View {
          fun onInitView()
          fun showErrorMessage(invalidCityMessage: String?)
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
        fun getWeatherData(textToBeSearched: String)
        fun destroyView()
        fun handleInfoResponse(todayWeatherResponse: TodayWeatherResponse?)
        fun firstLetterUppercase (string: String) : String
        fun getImageForCode(code: Int?) :Int
        fun formatSunriseSunsetDate(long: Long?) :String?
    }

    interface Model {
        fun fetchInvalidCityMessage(): String?
        fun initiateWeatherInfoCall(textToBeSearched: String): Observable<TodayWeatherResponse>
        fun setLanguageSystem(): String?

    }
}
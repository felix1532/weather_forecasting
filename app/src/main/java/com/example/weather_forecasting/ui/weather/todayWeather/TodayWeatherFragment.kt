package com.example.weather_forecasting.ui.weather.todayWeather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.weather_forecasting.R
import com.example.weather_forecasting.ui.WeatherContract
import com.example.weather_forecasting.ui.WeatherModelImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.today_weather_fragment.*


class TodayWeatherFragment : Fragment(), WeatherContract.TodayView {

    lateinit var presenter: WeatherContract.PresenterTodayWeather
    lateinit var model: WeatherContract.Model
    private lateinit var viewModel: TodayWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        model = context?.applicationContext?.let { WeatherModelImpl(it) }!!
        presenter = TodayWeatherForecastPresenterImpl(
                this,
                model,
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                context?.applicationContext!!
            )
        presenter.getGeolocation()

        return inflater.inflate(R.layout.today_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodayWeatherViewModel::class.java)


    }


    override fun onResume() {
        super.onResume()
        retry_main_view_fragment.setOnClickListener {
            presenter.getGeolocation()
            handleErrorView(false)
        }

    }

    override fun showErrorMessage(invalidCityMessage: Unit) {
        Toast.makeText(activity, "" +
                "", Toast.LENGTH_SHORT).show()
    }



    override fun setInfoCurrentDay(
        cityName: String?,
        temperature: Double,
        description: String?,
        sunset: String?,
        sunrise: String?,
        humidity: Int?,
        clouds: Int?,
        winSpeed: Double?,
        id: Int?,
        pressure: Int?,
        todayDate: String
    ) {
        textView_name_location.text = cityName
        textView_temperature.text= temperature.toInt().toString() + " ℃"
        textView_description.text = description
        textView_sunset.text = sunset
        textView_sunrise.text = sunrise
        textView_humidity.text = "${resources.getString(R.string.humidity)} " + humidity.toString() + "%"
        textView_speedWind.text = "${resources.getString(R.string.wind)} "+winSpeed?.toInt().toString() + "${resources.getString(R.string.met_sec)}"
        if (id != null) {
            imageView_condition_icon.setImageResource(id)
        }
        textView_pressure.text = pressure.toString()+ " ${resources.getString(R.string.hPa)}"
        updated_at.text =  "${resources.getString(R.string.date_update)}: " + todayDate



    }


    override fun handleLoaderView(showHandleLoader: Boolean) {
        if (showHandleLoader) {
            val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
            rotation.repeatCount = Animation.INFINITE
            rotation.repeatMode = Animation.RESTART
            loader.startAnimation(rotation)
            loader_main_view.visibility = View.VISIBLE
        } else {
            loader_main_view.visibility = View.GONE
        }
    }

    override fun handleWeatherView(showWeatherView: Boolean) {
        today_weather_fragment.visibility = if (showWeatherView) View.VISIBLE else View.GONE
    }

    override fun handleErrorView(showErrorView: Boolean) {
        retry_main_view_fragment.visibility = if (showErrorView) View.VISIBLE else View.GONE

    }



}


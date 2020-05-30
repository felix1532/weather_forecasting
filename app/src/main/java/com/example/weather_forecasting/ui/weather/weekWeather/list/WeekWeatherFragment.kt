package com.example.weather_forecasting.ui.weather.weekWeather.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.weekWeather.General
import com.example.weather_forecasting.ui.WeatherContract
import com.example.weather_forecasting.ui.WeatherModelImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.today_weather_fragment.loader
import kotlinx.android.synthetic.main.today_weather_fragment.loader_main_view
import kotlinx.android.synthetic.main.today_weather_fragment.retry_main_view_fragment
import kotlinx.android.synthetic.main.week_weather_fragment.*

class WeekWeatherFragment : Fragment(), WeatherContract.WeekView {


    lateinit var presenter: WeatherContract.PresenterWeekWeather
    lateinit var model: WeatherContract.Model
    private lateinit var viewModel: WeekWeatherViewModel



    companion object {
        fun newInstance(): WeekWeatherFragment = WeekWeatherFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        model = context?.applicationContext?.let { WeatherModelImpl(it) }!!
        presenter =
            WeekWeatherForecastPresenterImpl(
                this,
                model,
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                context?.applicationContext!!
            )
        presenter.getGeolocation()

        return inflater.inflate(R.layout.week_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeekWeatherViewModel::class.java)

    }

    override fun showErrorMessage(invalidCityMessage: Unit) {
        Toast.makeText(activity, "" +
                "", Toast.LENGTH_SHORT).show()
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
        day_recycler_view.visibility = if (showWeatherView) View.VISIBLE else View.GONE
    }

    override fun handleErrorView(showErrorView: Boolean) {
        retry_main_view_fragment.visibility = if (showErrorView) View.VISIBLE else View.GONE
    }


    override fun infoForecastDaysForWeekFragment(weekForecastingWeather: ArrayList<General?>, map: MutableMap< Int, String>) {
        day_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                RecyclerViewAdapter(
                    weekForecastingWeather,
                    map
                )

        }
    }
}

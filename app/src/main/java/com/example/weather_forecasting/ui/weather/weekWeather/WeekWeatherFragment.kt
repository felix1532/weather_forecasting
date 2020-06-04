package com.example.weather_forecasting.ui.weather.weekWeather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.network.PERMISSION_REQUEST
import com.example.weather_forecasting.model.modelWeekWeather.General
import com.example.weather_forecasting.ui.WeatherContract
import com.example.weather_forecasting.ui.weather.weekWeather.list.WeekWeatherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.today_weather_fragment.loader
import kotlinx.android.synthetic.main.today_weather_fragment.loader_main_view
import kotlinx.android.synthetic.main.week_weather_fragment.*


class WeekWeatherFragment : Fragment(), WeatherContract.WeekView {

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    lateinit var presenter: WeatherContract.PresenterWeekWeather
    private lateinit var viewModel: WeekWeatherViewModel

    companion object {
        fun newInstance(): WeekWeatherFragment = WeekWeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.week_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeekWeatherViewModel::class.java)
        enable_geolocation_week?.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST)
        }
        presenter = WeekWeatherForecastPresenterImpl(
                this,
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                context?.applicationContext!!
            )
        presenter.getDateFromGeolocation()

    }

    override fun onResume() {
        super.onResume()
        retry_main_view_fragment_week.setOnClickListener {
            presenter.getDateFromGeolocation()
        }
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED &&
            context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED)
        {
            presenter.getDateFromGeolocation()
        }
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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
        retry_main_view_fragment_week.visibility = if (showErrorView) View.VISIBLE else View.GONE
    }

    override fun showButtonEnableGeolocation(showButton: Boolean) {
        enable_geolocation_week.visibility = if(showButton) View.VISIBLE else View.GONE
    }


    override fun setInfoWeekDays(weekForecastingWeather: ArrayList<General?>, nameDays: MutableMap<Int, String>) {
        day_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter(
                    weekForecastingWeather,
                    nameDays
                )
        }
    }
}

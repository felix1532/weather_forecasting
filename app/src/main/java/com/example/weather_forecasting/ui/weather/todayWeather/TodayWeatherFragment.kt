package com.example.weather_forecasting.ui.weather.todayWeather

import android.Manifest
import android.content.Intent
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
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.network.response.PERMISSION_REQUEST
import com.example.weather_forecasting.ui.WeatherContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.today_weather_fragment.*


class TodayWeatherFragment : Fragment(), WeatherContract.TodayView {

    lateinit var presenter: WeatherContract.PresenterTodayWeather
    private lateinit var viewModel: TodayWeatherViewModel
    var infoShare: String = ""
    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.today_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodayWeatherViewModel::class.java)

        enable_geolocation?.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST)
        }
        presenter = TodayWeatherForecastPresenterImpl(
            this,
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            context?.applicationContext!!
        )
        presenter.getDateFromGeolocation()

        share_button.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, infoShare)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "${resources.getString(R.string.share)}: "))
        }


    }


    override fun onResume() {
        super.onResume()
        retry_main_view_fragment.setOnClickListener {
            presenter.getDateFromGeolocation()
        }

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            presenter.getDateFromGeolocation()
        }




    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val wifiManager:WifiManager = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        val reciever = object : BroadcastReceiver(){
//            override fun onReceive(context: Context?, intent: Intent?) {
//                val wifiStateExtra = intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
//                    WifiManager.WIFI_STATE_UNKNOWN)
//                WifiManager.WIFI_STATE_ENABLED
//            }
//        }
//
//
//    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun setInfoCurrentDay(
        cityName: String?,
        temperature: Double?,
        description: String?,
        sunset: String?,
        sunrise: String?,
        humidity: Int?,
        clouds: Int?,
        winSpeed: Double?,
        id: Int?,
        pressure: Int?,
        todayDate: String?
    ) {
        textView_name_location.text = cityName
        textView_temperature.text = temperature?.toInt().toString() + " ℃"
        textView_description.text = description
        textView_sunset.text = sunset
        textView_sunrise.text = sunrise
        textView_humidity.text =
            "${resources.getString(R.string.humidity)} " + humidity.toString() + "%"
        textView_speedWind.text = "${resources.getString(R.string.wind)} " + winSpeed?.toInt()
            .toString() + "${resources.getString(R.string.met_sec)}"
        if (id != null) {
            imageView_condition_icon.setImageResource(id)
        }

        textView_pressure.text = pressure.toString() + " ${resources.getString(R.string.hPa)}"
        updated_at.text = "${resources.getString(R.string.date_update)}: " + todayDate
        infoShare =
            "${resources.getString(R.string.city_name)}: $cityName - $temperature ℃, $description" +
                    "\n${resources.getString(R.string.time_sunset)}: $sunset" +
                    "\n${resources.getString(R.string.time_sunrise)}: $sunrise" +
                    "\n${resources.getString(R.string.humidity)}: $humidity%" +
                    "\n${resources.getString(R.string.wind)}: $winSpeed ${resources.getString(R.string.met_sec)}" +
                    "\n${resources.getString(R.string.pressure)}: $pressure ${resources.getString(R.string.hPa)}" +
                    "\n${resources.getString(R.string.date)}: $todayDate"
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

    override fun showButtonEnableGeolocation(showButton: Boolean) {
        enable_geolocation.visibility = if (showButton) View.VISIBLE else View.GONE
    }

}

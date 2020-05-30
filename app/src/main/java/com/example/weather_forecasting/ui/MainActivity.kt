package com.example.weather_forecasting.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weather_forecasting.R
import com.example.weather_forecasting.ui.weather.todayWeather.TodayWeatherFragment
import com.example.weather_forecasting.ui.weather.weekWeather.list.WeekWeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity(){

    val RequestPermissionCode = 1
    lateinit var navController: NavController
    lateinit var todayWeatherMainView: RelativeLayout
    lateinit var errorViewRL: RelativeLayout

    lateinit var today_weather_main_view: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        today_weather_main_view = findViewById(R.id.today_weather_main_view)
        errorViewRL = findViewById(R.id.retry_main_view)
        todayWeatherMainView = findViewById(R.id.today_weather_main_view)

        if (ActivityCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        }


        if (isOnline()) {
            todayWeatherMainView.visibility = View.VISIBLE
            navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            bottom_nav.setupWithNavController(navController)
            NavigationUI.setupActionBarWithNavController(this, navController)
        } else {
            errorViewRL.visibility = View.VISIBLE

        }
    }


    override fun onResume() {
        super.onResume()
        errorViewRL.setOnClickListener {

            if (isOnline()) {
                recreate()
            } else Toast.makeText(
                this,
                resources.getString(R.string.turn_internet),
                Toast.LENGTH_LONG
            ).show()
        }


    }

    fun isOnline(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
        this.recreate()
    }




}

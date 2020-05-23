package com.example.weather_forecasting.ui

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.db.entity.provider.LanguageProvider
import com.example.weather_forecasting.data.db.entity.provider.LanguageProviderImpl
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var todayWeatherMainView:RelativeLayout
    lateinit var loaderMainViewRL: RelativeLayout
    lateinit var loaderIV: ImageView
    lateinit var weatherForecastViewLL: LinearLayout
    lateinit var temperatureTV: TextView
    lateinit var cityNameTV: TextView

    lateinit var errorViewRL: RelativeLayout
    lateinit var searchCityET: EditText
    lateinit var activity: MainActivity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


//      loaderMainViewRL = findViewById(R.id.loader_main_view)
  //    loaderIV = findViewById(R.id.loader)
        errorViewRL = findViewById(R.id.retry_main_view)
        todayWeatherMainView = findViewById(R.id.today_weather_main_view)

        if(isOnline())
        {
            todayWeatherMainView.visibility = View.VISIBLE
            navController = Navigation.findNavController(this,R.id.nav_host_fragment)
            bottom_nav.setupWithNavController(navController)
            NavigationUI.setupActionBarWithNavController(this,navController)
        }else{
            errorViewRL.visibility = View.VISIBLE
        }


    }

    fun isOnline(): Boolean {
        val  connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val  networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo !=null && networkInfo.isConnected
    }
/*

    override fun handleLoaderView(showHandleLoader: Boolean) {
        if (showHandleLoader) {
            val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
            rotation.repeatCount = Animation.INFINITE
            rotation.repeatMode = Animation.RESTART
            loaderIV.startAnimation(rotation)
            loaderMainViewRL.visibility = View.VISIBLE
        } else {
            loaderMainViewRL.visibility = View.GONE
        }
    }

    override fun handleErrorView(showErrorView: Boolean) {
        errorViewRL.visibility = if (showErrorView) View.VISIBLE else View.GONE
    }
*/
    //clouds - облачность в процентах
    //wind.speed - скорость ветра
    //main.temp -температура
    //main.feels_like - ощущается температура человеком
    //main.humidity - влажностьй
}

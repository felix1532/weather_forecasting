package com.example.weather_forecasting.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weather_forecasting.R
import com.example.weather_forecasting.ui.weather.todayWeather.TodayWeatherFragment
import com.example.weather_forecasting.ui.weather.weekWeather.list.WeekWeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(){

    val todayWeatherFragment: Fragment = TodayWeatherFragment()
    val weekWeatherFragment: Fragment = WeekWeatherFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = todayWeatherFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navigationMenu: BottomNavigationView = findViewById(R.id.bottom_nav)
        navigationMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.main_container,todayWeatherFragment, "1").commit()
    }

    private val mOnNavigationItemSelectedListener:BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.todayWeatherFragment -> {
                        fm.beginTransaction().hide(active).show(todayWeatherFragment).commit()
                        active = todayWeatherFragment
                        return true
                    }
                    R.id.weekWeatherFragment -> {
                        if(!weekWeatherFragment.isAdded)
                        {
                            fm.beginTransaction().add(R.id.main_container, weekWeatherFragment, "2").hide(weekWeatherFragment).commit()
                        }
                        fm.beginTransaction().hide(active).show(weekWeatherFragment).commit()
                        active = weekWeatherFragment
                        return true
                    }

                }
                return false
            }

        }

}

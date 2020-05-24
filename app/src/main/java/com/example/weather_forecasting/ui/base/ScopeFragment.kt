package com.example.weather_forecasting.ui.base

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weather_forecasting.R
import com.example.weather_forecasting.ui.WeatherContract
import kotlinx.android.synthetic.main.today_weather_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopeFragment : Fragment(),CoroutineScope{

    private lateinit var job:Job



    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job =Job()
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }




}




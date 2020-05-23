package com.example.weather_forecasting.data.db.entity

import com.example.weather_forecasting.R


object WeatherToImage {
    fun getImageForCode(code: Int): Int = when (code) {
        200,230-> {
            R.drawable.clouds_with_lighting_littlerain_01
        }
        201,202-> {
            R.drawable.clouds_with_lighting_rain_01
        }
        210,211-> {
            R.drawable.clouds_with_lighting_01
        }
        212,221 -> {
            R.drawable.clouds_with_2lighting_01
        }
        231,232 -> {
            R.drawable.clouds_with_lighting_rain_01
        }
        300,301,302,310,311 -> {
            R.drawable.clouds_with_littlerain_01
        }
        312,313,314,321 -> {
            R.drawable.clouds_with_rain_01
        }
        500,501 -> {
            R.drawable.clouds_with_littlerain_01
        }
        502,503,504,511,520,521,522,531 -> {
            R.drawable.clouds_with_rain_01
        }
        600,601,602,611,612 -> {
            R.drawable.clouds_with_littlesnow_01
        }
        613,615,616,620,621,622 -> {
            R.drawable.clouds_with_snow_01
        }
        701,711,721,731,741,751,761,762,771,781,782,783,784,785,786,787,788,789,790,791,792,793,
        794,795,796,797,798,799-> {
            R.drawable.tornado
        }
        800 -> {
            R.drawable.clouds_01
        }
        801,802-> {
            R.drawable.sun_with_3clouds_01
        }
        803,804-> {
            R.drawable.clouds_01
        }

        else -> {
            R.drawable.sun_01
        }
    }
}
package com.example.weather_forecasting.ui

interface WeatherContract
{
    interface View
    {
        fun handleLoaderView(showHandleLoader: Boolean)
        fun handleErrorView (showHandleLoader: Boolean)
    }

    interface Presenter
    {

    }

    interface Model
    {

    }
}
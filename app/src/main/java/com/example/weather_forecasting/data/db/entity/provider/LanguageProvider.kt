package com.example.weather_forecasting.data.db.entity.provider

import com.example.weather_forecasting.internal.LanguageSystem

interface LanguageProvider {
    fun getLanguageSystem():LanguageSystem
}
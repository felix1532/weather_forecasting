package com.example.weather_forecasting.data.db.entity.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weather_forecasting.internal.LanguageSystem

const val LANGUAGE_SYSTEM = "LANGUAGE_SYSTEM"

class LanguageProviderImpl (context: Context): LanguageProvider {


    private val appContext = context.applicationContext

    private val preferences:SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getLanguageSystem(): LanguageSystem {
        val selectedName = preferences.getString(LANGUAGE_SYSTEM,LanguageSystem.ENGLISH.name)
        return LanguageSystem.valueOf(selectedName!!)
    }
}
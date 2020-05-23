package com.example.weather_forecasting.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.db.entity.provider.LANGUAGE_SYSTEM
import com.example.weather_forecasting.internal.LanguageSystem


class SettingsFragment:PreferenceFragmentCompat()
{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = resources.getString(R.string.setting)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }


}



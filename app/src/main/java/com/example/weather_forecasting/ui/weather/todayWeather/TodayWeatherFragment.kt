package com.example.weather_forecasting.ui.weather.todayWeather

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.weather_forecasting.R
import com.example.weather_forecasting.data.db.entity.WeatherToImage
import com.example.weather_forecasting.data.db.entity.provider.LANGUAGE_SYSTEM
import com.example.weather_forecasting.data.network.WeatherNetworkDataSourceImpl
import com.example.weather_forecasting.data.network.response.ConnectivityInterception
import com.example.weather_forecasting.data.network.response.ConnectivityInterceptionImpl
import com.example.weather_forecasting.data.network.response.OpenWeatherApiService
import com.example.weather_forecasting.internal.LanguageSystem
import com.example.weather_forecasting.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.today_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*


class TodayWeatherFragment : ScopeFragment() {

    private val mTimer: Timer? = null


    companion object {
        fun newInstance() =
            TodayWeatherFragment()
    }

    private lateinit var viewModel: TodayWeatherViewModel

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


        handleLoaderView(true)


        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
        val selectedName = preferences.getString(LANGUAGE_SYSTEM, LanguageSystem.ENGLISH.name)


        val apiService = OpenWeatherApiService(ConnectivityInterceptionImpl(this.requireContext()))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
        GlobalScope .launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchTodayWeather("London", setLanguageSystem(selectedName).toString(),"metric")
        }
        weatherNetworkDataSource.downloadedTodayWeather.observe(viewLifecycleOwner, Observer {

            handleLoaderView(false)
            updateLocation(it.name)
            updateInfoWeatherFragment(it.name,firstLetterUppercase(it.weather[0].description),it.main.temp.toInt(),it.main.feelsLike.toInt(),it.wind.speed,it.main.humidity,it.clouds.all)

            imageView_condition_icon.setImageResource(WeatherToImage.getImageForCode(it.weather[0].id))

        })





    }

    private fun updateLocation(location_name:String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = resources.getString(R.string.today)
    }

    private fun updateInfoWeatherFragment(location_name: String,description: String, temperatures:Int, feelsLike:Int, winSpeed:Double, humidity:Int,clouds: Int){
        textView_name_location.text = "$location_name"
        textView_description.text="$description"
        textView_temperature.text="$temperatures ℃"
        textView_feels_like_temperature.text="${resources.getString(R.string.feels_like)} $feelsLike ℃"
        textView_wind.text="${resources.getString(R.string.wind)} $winSpeed m/s"
        textView_humidity.text="${resources.getString(R.string.humidity)} $humidity %"
        text_view_clouds.text="${resources.getString(R.string.clouds)} $clouds %"
    }

    fun handleLoaderView(showHandleLoader: Boolean) {
        if (showHandleLoader) {
            val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
            rotation.repeatCount = Animation.INFINITE
            rotation.repeatMode = Animation.RESTART
            loader.startAnimation(rotation)
            loader_main_view.visibility = View.VISIBLE
        } else {
            loader_main_view.visibility = View.GONE
            today_weather_fragment.visibility = View.VISIBLE
        }
    }

    private fun firstLetterUppercase(string:String): String {
        var stringFLUppercase = ""
        stringFLUppercase += string.substring(0, 1).toUpperCase()
        for (i in 1 until string.length) {
            stringFLUppercase =
                if (" " == string.substring(i - 1, i)) stringFLUppercase + string.substring(i, i + 1)
                    .toUpperCase() else stringFLUppercase + string.substring(i, i + 1)
        }
        return stringFLUppercase
    }


    private fun setLanguageSystem(selectedName: String?): String? {
        var languageCode:String
        if(Locale.getDefault().language.equals("ru")){
            languageCode = "ru"
        }else languageCode="en"
        return languageCode
    }

    fun isOnline(): Boolean {
        val  connectivityManager = context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val  networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo !=null && networkInfo.isConnected
    }
}


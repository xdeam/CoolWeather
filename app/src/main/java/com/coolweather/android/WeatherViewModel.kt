package com.coolweather.android

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolweather.android.gson.mx.MWeatherInfo
import com.coolweather.android.http.Network
import com.coolweather.android.util.MyApplication
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    var weather = MutableLiveData<MWeatherInfo>()
    var cityId = MutableLiveData<String>()
    var refreshing = MutableLiveData<Boolean>()
    var weatherInitialized = MutableLiveData<Boolean>()


    init {
        cityId.value = "101010100"
    }

    fun getWeatherInfo() {
        launch ({
            weather.value = cityId.value?.let { Network.getInstance().fetchWeather(it) }
            weatherInitialized.value = true
        }, {
            Toast.makeText(MyApplication.getContext(), it.message, Toast.LENGTH_SHORT).show()
        })

    }
    fun refreshWeather() {
        refreshing.value = true
        launch ({
            weather.value = cityId.value?.let { Network.getInstance().fetchWeather(it) }
            refreshing.value = false
            weatherInitialized.value = true
        }, {
            Toast.makeText(MyApplication.getContext(), it.message, Toast.LENGTH_SHORT).show()
            refreshing.value = false
        })
    }
    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}
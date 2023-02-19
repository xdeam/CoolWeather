package com.coolweather.android.http

import com.coolweather.android.http.service.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Network {
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun fetchWeather(cityId: String) = weatherService.getMWeather(cityId).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {
        private var network: Network? = null
        fun getInstance(): Network {
            if (network == null) {
                synchronized(Network::class.java) {
                    if (network == null) {
                        network = Network()
                    }
                }
            }
            return network!!
        }

    }
}
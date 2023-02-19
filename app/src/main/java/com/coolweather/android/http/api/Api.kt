package com.coolweather.android.http.api

import okhttp3.HttpUrl

object Api {
    const val schema = "https"
    const val host = "aider.meizu.com"

    fun getBaseUrl():String{
        return HttpUrl.Builder().scheme(schema).host(host).toString();
    }
}

package com.coolweather.android.util;

public class WeatherApi {
    public static String HOST = "https://aider.meizu.com";
    public static String API_URL = "/app/weather/listWeather";
    public static String param = "?cityIds=";
    public static String defaultCityId = "101010100";

    public String Url(String cityId) {
        if (cityId != null && !cityId.isEmpty()){
            return HOST + API_URL + param + cityId;
        }
        return HOST + API_URL + param + defaultCityId;
    }

    interface Response{
        void onSuccess();
        void onError();
    }
}

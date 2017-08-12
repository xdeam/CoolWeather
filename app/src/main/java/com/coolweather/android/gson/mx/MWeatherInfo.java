
package com.coolweather.android.gson.mx;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MWeatherInfo {

    public String code;
    public String message;
    public String redirect;
    @SerializedName("value")
    public List<SingleCityWeather> valuesList;

}
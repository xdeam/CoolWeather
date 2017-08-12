package com.coolweather.android.gson.mx;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kwinter on 2017/8/11.
 */

public class SingleCityWeather {

    public List<Alarms> alarms;
    public String city;
    public int cityid;
    public List<Indexes> indexes;
    public Pm25 pm25;
    public String provinceName;
    public Realtime realtime;
    public WeatherDetailsInfo weatherDetailsInfo;
    @SerializedName("weathers")
    public List<RecentlyWeather> recentlyWeatherList;

    public class Indexes {

        public String abbreviation;
        public String alias;
        public String content;
        public String level;
        public String name;


    }

    public class Alarms {

        public String alarmContent;
        public String alarmDesc;
        public String alarmId;
        public String alarmLevelNo;
        public String alarmLevelNoDesc;
        public String alarmType;
        public String alarmTypeDesc;
        public String precaution;
        public String publishTime;

    }

    public class Pm25 {

        public String advice;
        public String aqi;
        public int citycount;
        public int cityrank;
        public String co;
        public String color;
        public String level;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String quality;
        public String so2;
        public String timestamp;
        public String upDateTime;
    }
}
package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kwinter on 2017/7/14.
 */

public class Country extends DataSupport{
    private int id;
    private String countryName;
    private int weatherId;
    private int cityId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }



}

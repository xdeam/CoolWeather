package com.coolweather.android.gson.mx;

import java.util.List;

/**
 * Created by kwinter on 2017/8/11.
 */
public class Location {

    private List<Areas> areas;
    public void setAreas(List<Areas> areas) {
        this.areas = areas;
    }
    public List<Areas> getAreas() {
        return areas;
    }
}
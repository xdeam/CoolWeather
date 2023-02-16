package com.coolweather.android.util;

import android.util.Log;

import com.coolweather.android.db.WeatherStored;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.gson.mx.Areas;
import com.coolweather.android.gson.mx.Location;
import com.coolweather.android.gson.mx.MWeatherInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by kwinter on 2017/8/9.
 */

public class RequestInfo implements Callback{
    private static RequestInfo instance;
    private static List<Areas> areasArrayList;
    public static int CITY_ID_NOT_EXIST=0;
    private static String responseText;
    private static MWeatherInfo mWeatherInfo;
    private boolean isLocked=true;
    private OnSuccess onSuccess;

    public void setOnSuccess(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
    }

    public static String getResponseText() {
        return responseText;
    }

    public static void setResponseText(String responseText) {
        RequestInfo.responseText = responseText;
    }

    public static MWeatherInfo getmWeatherInfo() {
        return mWeatherInfo;
    }

    public static void setmWeatherInfo(MWeatherInfo mWeatherInfo) {
        RequestInfo.mWeatherInfo = mWeatherInfo;
    }

    private   RequestInfo(){
        areasArrayList=getAreasListByGSON();
    }
    public static RequestInfo getInstance() {
        if (instance == null) {
            instance = new RequestInfo();
        }
        return instance;
    }
    public  int getAreasIdByCityName(String cityName){
        for (Areas areas:areasArrayList){
             cityName.equals(areas.getCountyname());
            return areas.getAreaid();
        }

        return CITY_ID_NOT_EXIST;
    }
    public static String rweather;
    public String getWeatherInfo(String weatherId){
        isLocked=true;
        String url="http://aider.meizu.com/app/weather/listWeather?cityIds="+weatherId;
        HttpUtil.sendOkHttpRequest(url, this);
           while (isLocked){}
        if (responseText!=null)
            return responseText;
        return null;
    }
    public static String requestWeather(final String weatherId){
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                rweather=null;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utilty.handleWeatherResponse(responseText);
                if(weather!=null&&"ok".equals(weather.status)){
                    rweather=responseText;
                    List<WeatherStored> list = DataSupport.where("weatherId=?",weatherId).find(WeatherStored.class);
                    WeatherStored weatherStored=null;
                    if (list.size()!=0)
                        weatherStored=list.get(0);
                }
                else{
                   rweather=null;
                }
            }



    });
        return rweather;
    }
    public  List locationArrayListByOriginal(){
        List <Areas> areasList=new ArrayList();
        try {
        InputStreamReader isr = new InputStreamReader(MyApplication.getContext().getAssets().open("Meizu_city.json"));
        //字节流转字符流
        BufferedReader bfr = new BufferedReader(isr);
        String line ;
        StringBuilder stringBuilder = new StringBuilder();
            while ((line = bfr.readLine())!=null){
                stringBuilder.append(line);
            }//将JSON数据转化为字符串
            JSONObject root = new JSONObject(stringBuilder.toString());
            //根据键名获取键值信息

            JSONArray array = root.getJSONArray("areas");
            bfr.close();
            isr.close();
            for (int i = 0;i < array.length();i++)
            {
                JSONObject stud = array.getJSONObject(i);
                Areas areas=new Areas();
                int areasId=stud.getInt("areaid");
                areas.setAreaid(areasId);
                String countryName=stud.getString("countyname");
                areas.setCountyname(countryName);
                areasList.add(areas);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    return areasList;
    }
    public List<Areas> getAreasListByGSON(){
    //    InputStreamReader isr = new InputStreamReader(MyApplication.getContext().getClassLoader().getResourceAsStream("/assets/" + "Meizu_city.json"));
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(MyApplication.getContext().getAssets().open("Meizu_city.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //字节流转字符流
        BufferedReader bfr = new BufferedReader(isr);
        String line ;
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        Location location=null;
        //创建JavaBean类的对象

        //用GSON方法将JSON数据转为单个类实体
        try {
            while ((line = bfr.readLine())!=null){
                stringBuilder.append(line);
            }
            location = gson.fromJson(stringBuilder.toString(), Location.class);
            List <Areas> areaslist= location.getAreas();
            //调用student方法展示解析的数据


        return areaslist;
    } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public  MWeatherInfo getMXWeather(String weatherId) {
        isLocked=true;
        String url = "http://aider.meizu.com/app/weather/listWeather?cityIds=" + weatherId;
        HttpUtil.sendOkHttpRequest(url, this);
        while (isLocked){}
        if (mWeatherInfo!=null&&"200".equals(mWeatherInfo.code))
            return mWeatherInfo;
        else Log.d("mErr",mWeatherInfo==null?"null":mWeatherInfo.message.toString());
        return null;
        }
    @Override
    public void onFailure(Call call, IOException e) {
        Log.d("ax1", "onFailure:");
        isLocked=false;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        responseText=response.body().string();
        mWeatherInfo=new Gson().fromJson(responseText, MWeatherInfo.class);
       isLocked=false;
       if (onSuccess!=null){
           onSuccess.dosomthing();
       }

    }
    public interface  OnSuccess{
        void dosomthing();
    }
}

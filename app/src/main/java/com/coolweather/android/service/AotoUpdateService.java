package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utilty;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AotoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int eightHour=8*60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+eightHour;
        Intent i=new Intent(this,AotoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }
    public void updateWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherSting=prefs.getString("weather",null);
        if (weatherSting!=null){
            final Weather weather= Utilty.handleWeatherResponse(weatherSting);
            final String weatherId=weather.basic.weatherId;
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseTest=response.body().string();
                    Weather weather1=Utilty.handleWeatherResponse(responseTest);
                    if (weather!=null&&"ok".equals(weather.status)){
                    SharedPreferences.Editor editor=PreferenceManager
                            .getDefaultSharedPreferences(AotoUpdateService.this).edit();
                        editor.putString("weather",responseTest);
                        editor.apply();
                    }
                }
            });
        }
    }
    private void updateBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AotoUpdateService.this)
                        .edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }

}

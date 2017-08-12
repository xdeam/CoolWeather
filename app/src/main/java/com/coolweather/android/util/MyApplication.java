package com.coolweather.android.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by kwinter on 2017/8/11.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        context=getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext(){
        return context;
    }
}

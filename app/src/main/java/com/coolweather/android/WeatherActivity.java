package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.mx.MWeatherInfo;
import com.coolweather.android.gson.mx.RecentlyWeather;
import com.coolweather.android.gson.mx.SingleCityWeather;
import com.coolweather.android.gson.mx.Weather3HoursDetailsInfos;
import com.coolweather.android.service.AotoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.MyApplication;
import com.coolweather.android.util.RequestInfo;
import com.coolweather.android.util.Utilty;
import com.coolweather.android.util.WeatherApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView weatherLayout;
    //private TextView titleCity;
    //   private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carwashText;
    private TextView sportText;
    private ImageView bingPicImg;
    //private Button navButton;
    public DrawerLayout drawerLayout;
    public LinearLayout drawPL;
    public AqiView aqiView;
    public Toolbar toolbar;
    public String weatherValueTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // navButton=(Button)findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        //  titleCity=(TextView)findViewById(R.id.title_city);
        // titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carwashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        drawPL = (LinearLayout) findViewById(R.id.tempLines);
        aqiView = (AqiView) findViewById(R.id.aqidiy);
        toolbar = (Toolbar) findViewById(R.id.toobar);

        //  SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        /* String weatherString=prefs.getString("weather",null);*/
        final String weatherId;
        weatherValueTrans = getIntent().getStringExtra("weatherTrans");
        if (weatherValueTrans != null) {
            Toast.makeText(WeatherActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
            MWeatherInfo mWeatherInfo = Utilty.handleMZWeatherRespnse(weatherValueTrans);
            SingleCityWeather singleCityWeather = mWeatherInfo.valuesList.get(0);
            weatherId = singleCityWeather.cityid + "";
          /*  weatherId=singleCityWeather.cityid+"";
            SharedPreferences.Editor editor=PreferenceManager
                    .getDefaultSharedPreferences(WeatherActivity.this)
                    .edit();
            editor.putString("weather",weatherTrans);
            editor.apply();*/
            Log.d("11", "showWeatherByWeatherTrans");
            showWeatherInfo(singleCityWeather);
        }
       /* else if (weatherString!=null){
            Log.d("11", "By WeatherString");
           Weather weather= Utilty.handleWeatherResponse(weatherString);
           // weatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
          //  toolbar.setTitle(weather.basic.cityName);
        }*/
        else {
            weatherId = getIntent().getStringExtra("weather_id");
            Log.d("11", "either both fail ");
            weatherLayout.setVisibility(View.INVISIBLE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   RequestInfo requestInfo= RequestInfo.getInstance();
                   requestInfo.getWeatherInfo(weatherId);
                   requestInfo.setOnSuccess(() -> {
                       MWeatherInfo mWeatherInfo= RequestInfo.getmWeatherInfo();
                       SingleCityWeather singleCityWeather = mWeatherInfo.valuesList.get(0);
                       showWeatherInfo(singleCityWeather);
                       weatherLayout.setVisibility(View.VISIBLE);
                   });
                }
            });

        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeatherApi weatherApi=new WeatherApi();
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.sendOkHttpRequest(weatherApi.Url(null), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       e.printStackTrace();
                                       Toast.makeText(MyApplication.getContext(),"更新失败>..<",Toast.LENGTH_SHORT).show();
                                       swipeRefreshLayout.setRefreshing(false);
                                   }
                               });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseText = response.body().string();
                                MWeatherInfo mWeatherInfo = new Gson().fromJson(responseText, MWeatherInfo.class);
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       showWeatherInfo(mWeatherInfo.valuesList.get(0));
                                       swipeRefreshLayout.setRefreshing(false);
                                       Toast.makeText(MyApplication.getContext(), "更新成功:)", Toast.LENGTH_SHORT).show();
                                   }
                               });
                            }
                        });

                    }
                }.run();

            }
        });
        //   String bingPic=prefs.getString("bing_pic",null);
    /*    if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }*/
        toolbar.setNavigationIcon(R.drawable.ic_bac);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

     /*   navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });*/
    }

    /* public void requestWeather(final String weatherId){
         String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";
         HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                         swipeRefreshLayout.setRefreshing(false);
                     }
                 });

             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 final String responseText=response.body().string();
                 final Weather weather=Utilty.handleWeatherResponse(responseText);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(weather!=null&&"ok".equals(weather.status)){
                             SharedPreferences.Editor editor=PreferenceManager
                                     .getDefaultSharedPreferences(WeatherActivity.this)
                                     .edit();
                             editor.putString("weather",responseText);
                             editor.apply();
                             List<WeatherStored> list = DataSupport.where("weatherId=?",weatherId).find(WeatherStored.class);
                             WeatherStored weatherStored=null;
                            if (list.size()!=0)
                            weatherStored=list.get(0);
                            else
                            weatherStored=new WeatherStored();
                             weatherStored.setCountyName(weather.basic.cityName);
                             weatherStored.setWeatherid(weatherId);
                             weatherStored.setWeatherString(responseText);
                             weatherStored.save();

                             toolbar.setTitle(weather.basic.cityName);
                            showWeatherInfo(weather);
                         }else {
                             Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                         }
                         swipeRefreshLayout.setRefreshing(false);
                     }
                 });
             }
         });
         loadBingPic();
     }*/
    public void showWeatherInfo(SingleCityWeather weather) {
        toolbar.setTitle(weather.city);
        String cityName = weather.city;
        String updateTime = weather.realtime.time;//updateTime.split(" ")[1];
        String degree = weather.realtime.temp + "°";
        String weatherInfo = weather.realtime.weather;
        // Log.d("weatherInfo",weather.now.more.info);
        //titleCity.setText(cityName);

//        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();


        for (RecentlyWeather recentlyWeather : weather.recentlyWeatherList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            ImageView weatherImg = (ImageView) view.findViewById(R.id.weather_pic);
            dateText.setText(recentlyWeather.date.toString().substring(5));
            infoText.setText(recentlyWeather.weather);
            //  maxText.setText(forecast.tempture.max);
            minText.setText(recentlyWeather.temp_day_c);
            // Log.e("errCODE",""+getImageResourceId("a"+recentlyWeather.img));
            Log.d("CODE", recentlyWeather.img);

            int resId = getResources().getIdentifier("a" + recentlyWeather.img, "drawable", getPackageName());
            Log.d("resCODE", resId + "");
            //int real=R.drawable.a100;
            // Log.d("REalCODE",""+real);
            weatherImg.setImageResource(resId);

            forecastLayout.addView(view);
        }
        drawPL.removeAllViews();
        TeptView teptView = new TeptView(WeatherActivity.this);
        List<Integer> maxTemplist = new ArrayList<>();
        List<Integer> minTemplist = new ArrayList<>();
        for (Weather3HoursDetailsInfos weather3HoursDetailsInfos : weather.weatherDetailsInfo.weather3HoursDetailsInfos) {
            maxTemplist.add(Integer.valueOf(weather3HoursDetailsInfos.highestTemperature));
            minTemplist.add(Integer.valueOf(weather3HoursDetailsInfos.lowerestTemperature));
        }

        teptView.setMaxTempList(maxTemplist);
        teptView.setMinTempList(minTemplist);
        drawPL.addView(teptView);

        if (weather.pm25.aqi != null) {
            aqiView.setText(weather.pm25.quality + " " + weather.pm25.aqi);
            aqiText.setText(weather.pm25.aqi);
            pm25Text.setText(weather.pm25.pm25);
        }
        String comfort = "感冒指数 " + weather.indexes.get(1).content;
        String carWash = "洗车指数 " + weather.indexes.get(2).content;
        String sport = "运动建议 " + weather.indexes.get(5).content;
        comfortText.setText(comfort);
        carwashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AotoUpdateService.class);
        startService(intent);
    }

    private void loadBingPic() {
        Log.i("loadBingPic", "log");
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("picErr", "ERR");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                Log.d("picUrl", bingPic);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

//    public int getImageResourceId(String name) {
//        R.drawable drawables = new R.drawable();
//        //默认的id
//        int resId = 0x7f02000b;
//        try {
//            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
//            java.lang.reflect.Field field = R.drawable.class.getField(name);
//            //取值
//            resId = (Integer) field.get(drawables);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return resId;
//    }

    @Override
    protected void onStop() {
        Log.d("tt", "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("tt", "des: ");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
        editor.putString("weather", weatherValueTrans);
        editor.apply();
        super.onDestroy();
    }
}

package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.coolweather.android.util.RequestInfo;

public class MainActivity extends AppCompatActivity {
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherTrans = prefs.getString("weather", null);
        if (weatherTrans != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weatherTrans", weatherTrans);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id","101010100");
            startActivity(intent);
            finish();
//            initAMapLocationListener();
//            initLocation();
        }

    }

    /**
     * 获取高德地图定位城市
     */
    private void initAMapLocationListener() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        String city = amapLocation.getCity();
                        if (!TextUtils.isEmpty(city)) {
                            String cityName = city.replace("市", "");
                            Log.i("定位成功", "当前城市为" + cityName);
                            Toast.makeText(MainActivity.this, "当前城市" + cityName, Toast.LENGTH_SHORT).show();
                            int weatherId = RequestInfo.getInstance().getAreasIdByCityName(cityName);
                            if (RequestInfo.getInstance().getWeatherInfo(weatherId + "") != null) {
                                String weatherTrans = RequestInfo.getResponseText();
                                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                                intent.putExtra("weatherTrans", weatherTrans);
                                startActivity(intent);
                                finish();
                            }

                        }

                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("aMapError", "ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                        Log.e("定位失败", "");
                        Toast.makeText(MainActivity.this, "定位失败〒_〒", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ChoosePosition.class);
                        startActivity(intent);
                        finish();


                    }
                    //停止定位
                    mLocationClient.stopLocation();
                    //销毁定位
                    mLocationClient.onDestroy();
                }
            }
        };
    }


    /**
     * 初始化高德地图定位参数
     */
    private void initLocation() {
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }


    /**
     * 点击返回键两次退出程序
     */
    private long exitTime = 0;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
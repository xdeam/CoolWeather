package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kwinter on 2017/8/6.
 */

public class Hforecast {
    @SerializedName("cond")
    public Cond cond;

    class Cond{
       public String code;
        public String txt
    }
    public String date  //时间
    public String hum//相对湿度（%）
    public String pop//降水概率
    public String pres//气压
    public String tmp//温度
}

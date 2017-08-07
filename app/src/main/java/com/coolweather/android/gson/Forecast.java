package snow.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kwinter on 2017/7/15.
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Tempture tempture;
    @SerializedName("cond")
    public More more;
    public class Tempture{
        public  String max;
        public  String min;
    }
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}

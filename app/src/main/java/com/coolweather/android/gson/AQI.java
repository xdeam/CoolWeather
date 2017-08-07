package snow.coolweather.android.gson;

/**
 * Created by kwinter on 2017/7/15.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}

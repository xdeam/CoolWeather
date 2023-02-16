package com.coolweather.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coolweather.android.db.WeatherStored;
import com.coolweather.android.gson.mx.Areas;
import com.coolweather.android.gson.mx.MWeatherInfo;
import com.coolweather.android.search.SearchAdapter;
import com.coolweather.android.search.SearchView;
import com.coolweather.android.search.SearchView.SearchViewListener;
import com.coolweather.android.util.RequestInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChoosePosition extends AppCompatActivity implements SearchViewListener{

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    private List<Areas> dbData;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<Areas> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
       ChoosePosition.hintSize = hintSize;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.parseColor("#006699"));
        }

        setContentView(R.layout.activity_choose_position);
        initData();
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
              //  Toast.makeText(ChoosePosition.this, "position:" +((Areas)adapterView.getItemAtPosition(position)).getAreaid(), Toast.LENGTH_SHORT).show();

                int areaid=((Areas)adapterView.getItemAtPosition(position)).getAreaid();
                MWeatherInfo mWeatherInfo= RequestInfo.getInstance().getMXWeather(areaid+"");
                if (mWeatherInfo!=null)
                {
                    String weatherTrans=RequestInfo.getResponseText();
                    List<WeatherStored> list = DataSupport.where("areaId=?",areaid+"").find(WeatherStored.class);
                    WeatherStored weatherStored=null;
                    if (list.size()!=0)
                        weatherStored=list.get(0);
                    else
                        weatherStored=new WeatherStored();
                    weatherStored.setCityName(((Areas) adapterView.getItemAtPosition(position)).getCountyname());
                    weatherStored.setAreaId(areaid);
                    weatherStored.setWeatherString(weatherTrans);

                    weatherStored.save();
                Intent intent=new Intent(ChoosePosition.this,WeatherActivity.class);
                intent.putExtra("weatherTrans",weatherTrans);
                startActivity(intent);
                finish();
                }

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbData() {
        dbData= RequestInfo.getInstance().getAreasListByGSON();
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<>();
       /* for (int i = 1; i <= hintSize; i++) {
            hintData.add("热搜" + i + "：Android自定义View");
        }*/
       hintData.add("北京");
       hintData.add("天津");
        hintData.add("上海");
        hintData.add("广州");
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getCountyname().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i).getCountyname());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getCountyname().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "完成搜索", Toast.LENGTH_SHORT).show();


    }

}


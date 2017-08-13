package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.WeatherStored;
import com.coolweather.android.gson.mx.MWeatherInfo;
import com.coolweather.android.util.Utilty;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kwinter on 2017/8/8.
 */
public class DrawnFragment extends Fragment{
    LinearLayout citymanage;
    ListView listView;
    ArrayAdapter arrayAdapter;
    List datalist=new ArrayList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drawn_fragment,container,false);
        citymanage=(LinearLayout) view.findViewById(R.id.citymanage);
        listView=(ListView) view.findViewById(R.id.cityList);
        arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_expandable_list_item_1,datalist);
        listView.setAdapter(arrayAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String ss="";
        dataChanged();
        citymanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),ChoosePosition.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    public void dataChanged(){
        final List<WeatherStored> list=DataSupport.findAll(WeatherStored.class);
        datalist.clear();
        for (WeatherStored weatherStored:list){
            datalist.add(weatherStored.getCityName());
        }
        final List ulist=list;
        arrayAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherActivity weatherActivity=(WeatherActivity) getActivity();
                weatherActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                WeatherStored weatherStored=list.get(position);
               MWeatherInfo mWeatherInfo= Utilty.handleMZWeatherRespnse(weatherStored.getWeatherString());

                weatherActivity.weatherValueTrans=weatherStored.getWeatherString();
                weatherActivity.showWeatherInfo(mWeatherInfo.valuesList.get(0));

            }

        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherStored weatherStored=list.get(position);
                weatherStored.delete();
                Toast.makeText(getContext(),"成功删除",Toast.LENGTH_SHORT);
                dataChanged();
                return true;
            }
        });
    }
}

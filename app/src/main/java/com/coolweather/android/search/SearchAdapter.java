package com.coolweather.android.search;

import android.content.Context;

import com.coolweather.android.R;
import com.coolweather.android.gson.mx.Areas;

import java.util.List;

/**
 * Created by yetwish on 2015-05-11
 */

public class SearchAdapter extends CommonAdapter<Areas>{

    public SearchAdapter(Context context, List<Areas> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder
                .setText(R.id.item_search_tv_title,mData.get(position).getCountyname());
    }
}

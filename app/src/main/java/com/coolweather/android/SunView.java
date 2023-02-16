package com.coolweather.android;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by kwinter on 2017/8/7.
 */

public class SunView extends View {
    public String sunRiseTime;
    public String sunDownTime;
    public Paint  yPaint;
    public Paint  texPaint;
    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

}

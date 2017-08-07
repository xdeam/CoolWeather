package com.coolweather.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by kwinter on 2017/8/6.
 */

public class AqiView extends TextView {
    Paint mPaint;
    public AqiView(Context context) {
        super(context);
        initPaint();
    }

    public AqiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }
    public void initPaint(){
        mPaint=new Paint();
        mPaint.setAlpha(100);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height =getMeasuredHeight();


        Log.d("numbers","letf:"+getLeft()+"right"+getRight()+"top:"+getTop());
        canvas.drawRoundRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),20,30, mPaint);

        super.onDraw(canvas);
    }
}

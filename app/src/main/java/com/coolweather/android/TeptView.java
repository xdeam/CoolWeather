package com.coolweather.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kwinter on 2017/8/1.
 */

public class TeptView extends View {
    private Context context;
    private Paint pointPaint,textPaint,textPaint2,linePaint;
    //坐标点画笔，文字画笔，线画笔
    private float textHeight;//文字高度
    private int scale=10;//一度对应多少像素
    private int radius=5;//温度坐标点的半径
    private int xSpace=60;//横坐标点之间的间隔
    private int ySpace=5;//温度文字与图标间的垂直间隔
    private int[] x=new int[7];//一共有多少个温度值
    private String [] weekDay=new String[5];//显示的星期信息
    private int centerTemp;
    private int centerHeight;
    private List <Integer>minTempList;
    private List <Integer>maxTempList;

    public List getMinTempList() {
        return minTempList;
    }

    public void setMinTempList(List minTempList) {
        this.minTempList = minTempList;
    }

    public List getMaxTempList() {
        return maxTempList;
    }

    public void setMaxTempList(List maxTempList) {
        this.maxTempList = maxTempList;
    }

    public TeptView(Context context) {
        super(context);
        init();
    }

    public TeptView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }
    public void init(){//执行初始化操作
        //坐标点画笔的初始化
        pointPaint=new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.DKGRAY);
        //连接线画笔的初始化
        linePaint=new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(3);//宽度为3px
        linePaint.setStyle(Paint.Style.FILL);//填充
        //文本画笔初始化
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.DKGRAY);//文本为黑色
        textPaint.setTextSize(20);//文本大小为16
        textPaint2=new Paint();
        textPaint2.setAntiAlias(true);
        textPaint2.setColor(Color.RED);
        textPaint.setTextSize(14);
        //计算文字高度
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        textHeight=fontMetrics.bottom-fontMetrics.top;
      /*  Calendar calendar=Calendar.getInstance();//获取当前时间
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);//获取当前星期
        */
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=this.getWidth();  //获取控件的宽度
        xSpace=2*width/(maxTempList.size()+minTempList.size());      //计算两个点之间的x轴间距
        for (int i=0;i<(maxTempList.size()+minTempList.size())/2;i++){
            x[i]=40+i*xSpace;
        }


        List dateList=new ArrayList();


       if (maxTempList.size()!=0&&minTempList.size()!=0)
         centerTemp=((int) Collections.max(maxTempList)+ (int) Collections.min(minTempList))/2;
       else if (maxTempList.size()!=0){
           centerTemp=((int) Collections.max(maxTempList)+ (int) Collections.min(maxTempList))/2;
       }else if (minTempList.size()!=0){
           centerTemp=((int) Collections.max(minTempList)+ (int) Collections.min(minTempList))/2;
       }else {
           return;
       }
        Log.d("centerTemp",centerTemp+"");
         centerHeight=this.getHeight()/2;//获取控件的Y轴中线，中线
        Log.d("centerHeight", "onDraw: "+centerHeight);

        drawPL(maxTempList,canvas,true);
        drawPL(minTempList,canvas,false);


    }
      public void  drawPL(List list,Canvas canvas,boolean isTop){
          Log.d("size",maxTempList.size()+"");
          for (int i=0;i<list.size();i++){
              int temp=(int)list.get(i);
              float point=(-(temp-centerTemp))*scale;//该点相对于中线的纵坐标
              canvas.drawCircle(x[i],centerHeight+point,radius,pointPaint);
              if (isTop)canvas.drawText(temp+"°",x[i]-12,centerHeight+point-textHeight/2-ySpace,textPaint);
              else canvas.drawText(temp+"°",x[i]-12,centerHeight+point+textHeight+ySpace,textPaint);
              if (i!=list.size()-1){
                  int nextTemp=(int)list.get(i+1);
                  float pointNext=(-(nextTemp-centerTemp))*scale;//该点相对于中线的纵坐标
                  canvas.drawLine(x[i],centerHeight+point,x[i+1],centerHeight+pointNext,linePaint);

              }
          }


      }


}

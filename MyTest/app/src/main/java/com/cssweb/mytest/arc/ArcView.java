package com.cssweb.mytest.arc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2016/5/21.
 */
public class ArcView extends View {
    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();    //采用默认设置创建一个画笔
        paint.setAntiAlias(true);//使用抗锯齿功能
        paint.setColor(0xFFA4C739);    //设置画笔的颜色为绿色paint.setStyle(Paint.Style.FILL);//设置画笔类型为填充
        /***********绘制圆弧*************/
        RectF rectf_head = new RectF(10, 10, 100, 100);//确定外切矩形范围
        rectf_head.offset(100, 20);//使rectf_head所确定的矩形向右偏移100像素，向下偏移20像素
        canvas.drawArc(rectf_head,0, -90, true, paint);//绘制圆弧，不含圆心
    }
}

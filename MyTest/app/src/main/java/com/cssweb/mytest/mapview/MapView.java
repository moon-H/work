package com.cssweb.mytest.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.cssweb.mytest.Utils;

/**
 * Created by lenovo on 2016/1/24.
 */
public class MapView extends View {
    private Context context;

    public MapView(Context context) {
        super(context);
        init(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    Matrix matrix1 = new Matrix();
    Matrix matrix2 = new Matrix();
    Matrix matrix3 = new Matrix();
    Matrix matrix4 = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap1_1 = Utils.getBitmapFromAsset(context, "6_1.png");
        Bitmap bitmap1_2 = Utils.getBitmapFromAsset(context, "6_2.png");
        Bitmap bitmap2_1 = Utils.getBitmapFromAsset(context, "7_1.png");
        Bitmap bitmap2_2 = Utils.getBitmapFromAsset(context, "7_2.png");
        matrix1.reset();
        matrix2.reset();
        matrix3.reset();
        matrix4.reset();
        matrix1.postTranslate(0, 0);
        canvas.drawBitmap(bitmap1_1, matrix1, null);
        matrix2.postTranslate(256, 0);
        canvas.drawBitmap(bitmap1_2, matrix2, null);
        matrix3.postTranslate(0, 256);
        canvas.drawBitmap(bitmap2_1, matrix3, null);
        matrix4.postTranslate(256, 256);
        canvas.drawBitmap(bitmap2_2, matrix4, null);
    }
}

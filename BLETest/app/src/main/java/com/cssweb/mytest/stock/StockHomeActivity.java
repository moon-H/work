package com.cssweb.mytest.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.cssweb.mytest.R;
import com.cssweb.mytest.utils.MLog;

/**
 * Created by liwx on 2020-03-16.
 * 计算股票交易手续费等
 */
public class StockHomeActivity extends FragmentActivity {
    private static final String TAG = "StockHomeActivity";

    private float YIN_HUA_SHUI = 0.001f;//印花税
    private float GUO_HU_FEI = 1;//过户费
    private float GUO_H = 1;//过户费


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_main);
        MLog.d(TAG, "onCreate");


    }
}

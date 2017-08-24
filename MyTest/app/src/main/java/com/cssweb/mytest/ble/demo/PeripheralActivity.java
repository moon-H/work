package com.cssweb.mytest.ble.demo;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.ble.tool.PeripheralManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lenovo on 2017/4/26.
 */

public class PeripheralActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "PeripheralActivity";

    private static final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());

    PeripheralManager mPeripheralManager;
    TextView mTvStatus;
    TextView mTvTotalTime;

    StringBuilder mStatusBuilder = new StringBuilder();
    StringBuilder mAdvTimeBuilder = new StringBuilder();

    private long startTimeMilles;//广播开启时间
    private long endTimeMilles;//广播成功时间

    private static final String ADV_DATA = "8888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_peripheral);
        Log.d(TAG, "onCreate");
        mPeripheralManager = new PeripheralManager(PeripheralActivity.this);
        registerReceiver();
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mTvTotalTime = (TextView) findViewById(R.id.tv_time);
        findViewById(R.id.btn_start_adv).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        LocalBroadcastManager.getInstance(PeripheralActivity.this).unregisterReceiver(mReceiver);
        mPeripheralManager.stopAdvertise();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PeripheralManager.ACTION_DEVICE_CONNECD);
        filter.addAction(PeripheralManager.ACTION_DEVICE_DISCONNECT);
        filter.addAction(PeripheralManager.ACTION_ADVERTISE_START);
        filter.addAction(PeripheralManager.ACTION_ADVERTISE_SUCCESS);
        filter.addAction(PeripheralManager.ACTION_ADVERTISE_FAILED);
        LocalBroadcastManager.getInstance(PeripheralActivity.this).registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action))
                if (action.equals(PeripheralManager.ACTION_ADVERTISE_START)) {
                    mStatusBuilder.append(dateFomatter.format(System.currentTimeMillis()) + "  " + "开始广播" + "\n");
                } else if (action.equals(PeripheralManager.ACTION_ADVERTISE_FAILED)) {
                    mStatusBuilder.append(dateFomatter.format(System.currentTimeMillis()) + "  " + "广播失败" + "\n");
                } else if (action.equals(PeripheralManager.ACTION_ADVERTISE_SUCCESS)) {
                    mStatusBuilder.append(dateFomatter.format(System.currentTimeMillis()) + "  " + "广播成功" + "\n");
                    endTimeMilles = System.currentTimeMillis();
                    mAdvTimeBuilder.append("总耗时 = " + (endTimeMilles - startTimeMilles) + " ms" + "\n");

                } else if (action.equals(PeripheralManager.ACTION_DEVICE_CONNECD)) {
                    mStatusBuilder.append(dateFomatter.format(System.currentTimeMillis()) + "  " + "有设备连接上" + "\n");
                    //                    mPeripheralManager.notifyData();
                }
            mTvStatus.setText(mStatusBuilder.toString());
            mTvTotalTime.setText(mAdvTimeBuilder.toString() + "  " + ADV_DATA);
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_adv:
                mStatusBuilder.setLength(0);
                startTimeMilles = System.currentTimeMillis();
                mPeripheralManager.startAdvertise(HexConverter.hexStringToBytes(ADV_DATA));
                break;
        }
    }


}
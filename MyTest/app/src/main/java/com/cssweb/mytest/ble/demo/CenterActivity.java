package com.cssweb.mytest.ble.demo;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.ble.DataContainer;
import com.cssweb.mytest.ble.tool.CenterManager2;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lenovo on 2017/4/26.
 */

public class CenterActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "CenterActivity";
    private CenterManager2 mCenterManager;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
    private TextView mTvStartScan;//开始扫描
    private TextView mTvFindDevice;//发现设备
    private TextView mTvCommonStatus;//通用状态
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder waistTimeBuild = new StringBuilder();
    private TextView mTvTotalTime;

    private long startTimeMilles;//连接开始时间
    private long endTimeMilles;//完成连接时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_center);
        mCenterManager = new CenterManager2(CenterActivity.this);
        mTvStartScan = (TextView) findViewById(R.id.tv_start_scan);
        mTvCommonStatus = (TextView) findViewById(R.id.tv_common_status);
        findViewById(R.id.btn_start_scan).setOnClickListener(this);
        mTvTotalTime = (TextView) findViewById(R.id.time);
        registerReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_scan:
                stringBuilder.setLength(0);
                startTimeMilles = System.currentTimeMillis();
                mCenterManager.disconnect();
                mCenterManager.startScanLeDevice(true);
                //                mCenterManager.startScanOldApi();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCenterManager.stopScan();
                    }
                }, 3000);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(CenterActivity.this).unregisterReceiver(mReceiver);
        mCenterManager.stopScan();
        mCenterManager.disconnect();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CenterManager2.ACTION_GATT_CONNECTTING);
        filter.addAction(CenterManager2.ACTION_GATT_CONNECTED);
        filter.addAction(CenterManager2.ACTION_GATT_DISCONNECTED);
        filter.addAction(CenterManager2.ACTION_GATT_CONNECT_TIME_OUT);
        filter.addAction(CenterManager2.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(CenterManager2.ACTION_FIND_DEVICE);
        filter.addAction(CenterManager2.ACTION_START_SCAN);
        filter.addAction(CenterManager2.ACTION_STOP_SCAN);
        filter.addAction(CenterManager2.ACTION_DATA_AVAILABLE);
        filter.addAction(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL);
        filter.addAction(CenterManager2.ACTION_WRITE_DATA_RESULT);
        filter.addAction(CenterManager2.ACTION_RECEIVE_DATA);
        LocalBroadcastManager.getInstance(CenterActivity.this).registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(CenterManager2.ACTION_GATT_CONNECTTING)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + "   ConnectGatt" + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_GATT_CONNECTED)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + "   ConnectGatt SUCCESS. Start DiscoverServices" + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_GATT_SERVICES_DISCOVERED)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + "   DiscoverServices SUCCESS. Send Data" + "\n" + "\n" + "\n");
                    mCenterManager.writeCustomData(HexConverter.hexStringToBytes("9988"));

                } else if (action.equals(CenterManager2.ACTION_GATT_CONNECT_TIME_OUT)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + " 连接超时" + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_GATT_DISCONNECTED)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + " 连接断开" + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_START_SCAN)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + " 扫描中" + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_STOP_SCAN)) {
                    //                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + " 停止扫描" + "\n");
                } else if (action.equals(CenterManager2.ACTION_FIND_DEVICE)) {
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + " 发现设备" + intent.getStringExtra("value") + "\n" + "\n" + "\n");
                    ScanResult device = DataContainer.scanResult;
                    mCenterManager.connect(device.getDevice().getAddress());
                    //                    mCenterManager.connect(DataContainer.oldApiFindDevice.getAddress());

                } else if (action.equals(CenterManager2.ACTION_DATA_AVAILABLE)) {

                } else if (action.equals(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL)) {
                    String newData = intent.getStringExtra("value");
                    Log.d(TAG, "value = " + newData);
                } else if (action.equals(CenterManager2.ACTION_WRITE_DATA_RESULT)) {
                    String result = intent.getStringExtra("value");
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + "  发送消息：" + "  结果：" + result + "\n" + "\n" + "\n");
                } else if (action.equals(CenterManager2.ACTION_RECEIVE_DATA)) {
                    String value = intent.getStringExtra("value");
                    stringBuilder.append(dateFormatter.format(System.currentTimeMillis()) + "  收到消息：" + "  内容：" + value + "\n" + "\n" + "\n");
                    endTimeMilles = System.currentTimeMillis();
                    waistTimeBuild.append("总耗时 = " + (endTimeMilles - startTimeMilles) + " ms" + "\n" + "\n");
                    mTvTotalTime.setText(waistTimeBuild.toString());
                }
                mTvCommonStatus.setText(stringBuilder.toString());
            }
        }
    };
}

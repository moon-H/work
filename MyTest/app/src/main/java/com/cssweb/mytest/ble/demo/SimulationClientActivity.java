package com.cssweb.mytest.ble.demo;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.ble.tool.CenterManagerCommon;
import com.cssweb.mytest.ble.tool.PeripheralManager;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by lenovo on 2017/5/1.
 */

public class SimulationClientActivity extends FragmentActivity {
    private static final String TAG = "SimulationCActivity";
    private CenterManagerCommon mCenterManager;
    PeripheralManager mPeripheralManager;
    private TextView mTvData;
    private StringBuilder mStringBuilder = new StringBuilder();
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
    public static final UUID UUID_SERVICE_EXIT_GATE = UUID.fromString("386a4aed-1bcb-4aac-a599-718f78f7d27e");
    public static final UUID UUID_CHARACTERISTIC_DATA_SHARE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");//用于广播设备编号
    public static final UUID UUID_SERVICE_ENTRY_GATE = UUID.fromString("516d165d-8c96-4dda-83c0-9f1f21cff966");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        Log.d(TAG, "onCreate");
        mPeripheralManager = new PeripheralManager(SimulationClientActivity.this);
        mCenterManager = new CenterManagerCommon(SimulationClientActivity.this);
        mTvData = (TextView) findViewById(R.id.tv_receive_data);
        mCenterManager.startScanLeDevice(UUID_SERVICE_ENTRY_GATE);
        //        mHandler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                mCenterManager.stopScanOldApi();
        //                mCenterManager.startScanOldApi();
        //            }
        //        }, 500);
        //        countDownTimer.start();

        findViewById(R.id.btn_start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        registerReceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTime();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCenterManager.stopScan();
        LocalBroadcastManager.getInstance(SimulationClientActivity.this).unregisterReceiver(mReceiver);
        mPeripheralManager.stopAdvertise();
        mCenterManager.stopScanOldApi();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CenterManagerCommon.ACTION_FIND_TARGET_DEVICE);
        LocalBroadcastManager.getInstance(SimulationClientActivity.this).registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(CenterManagerCommon.ACTION_FIND_TARGET_DEVICE)) {
                    //                    mCenterManager.stopScanOldApi();
                    //                    byte[] byteData = HexConverter.hexStringToBytes(intent.getStringExtra("value"));
                    //                    Log.d(TAG, "data =" + intent.getStringExtra("value") + "00");
                    ScanResult scanResult = (intent.getParcelableExtra("scanResult"));
                    if (scanResult != null) {
                        int findDeviceCode = getDeviceCode(scanResult.getScanRecord());
                        mStringBuilder.append(getTimeStamp() + "  设备编号  = " + findDeviceCode + "\n");
                    } else {
                        mStringBuilder.append(getTimeStamp() + "  未扫描到设备 " + "\n");
                    }


                    //                    mPeripheralManager.startAdvertise(byteData);
                }
            }
            mTvData.setText(mStringBuilder.toString());
        }

    };

    private String getTimeStamp() {
        return dateFormatter.format(System.currentTimeMillis());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanRecord getScanRecord(ScanResult scanResult) {
        if (scanResult != null) {
            return scanResult.getScanRecord();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int getDeviceCode(ScanRecord scanRecord) {
        byte[] deviceCode = scanRecord.getServiceData().get(new ParcelUuid(UUID_CHARACTERISTIC_DATA_SHARE));
        String deviceCodeHex = HexConverter.bytesToHexString(deviceCode);
        Log.d(TAG, "deviceCodeHex = " + deviceCodeHex);
        int findDeviceCode = Integer.valueOf(deviceCodeHex);
        Log.d(TAG, "findDeviceCode = " + findDeviceCode);
        return findDeviceCode;
    }

    private void getTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            NTPUDPClient timeClient = new NTPUDPClient();
            String timeServerUrl = "202.120.2.101";
            InetAddress timeServerAddress = null;
            timeServerAddress = InetAddress.getByName(timeServerUrl);
            TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
            TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
            TimeStamp timeStamp1 = timeInfo.getMessage().getOriginateTimeStamp();

            Log.d(TAG, "时间差= " + (timeStamp1.getDate().getTime() - timeStamp.getDate().getTime()));
            Log.d(TAG, "时间1= " + dateFormat.format(timeStamp1.getDate().getTime()));
            Log.d(TAG, "时间2= " + dateFormat.format(timeStamp.getDate().getTime()));

            Date date = timeStamp.getDate();
            //            System.out.println(dateFormat.format(date));
            Log.d(TAG, "服务器时间= " + dateFormat.format(date));
            Log.d(TAG, "当前时间= " + dateFormat.format(System.currentTimeMillis()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

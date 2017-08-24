package com.cssweb.mytest.ble.demo;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.cssweb.mytest.CommonToast;
import com.cssweb.mytest.R;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;

import java.util.UUID;

/**
 * Created by lenovo on 2017/4/26.
 */

public class BleDemoMainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "BleDemoMainActivity";
    BluetoothClient mClient;
    public static final UUID CUSTOM_UUID = UUID.fromString("516d165d-8c96-4dda-83c0-9f1f21cff966");
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        findViewById(R.id.btn_center).setOnClickListener(this);
        findViewById(R.id.btn_device).setOnClickListener(this);
        mClient = new BluetoothClient(getApplicationContext());
        if (!mClient.isBluetoothOpened()) {
            CommonToast.toast(getApplicationContext(), "开启蓝牙");
            mClient.openBluetooth();
        }
        mClient.registerBluetoothStateListener(mBluetoothStateListener);
        String macAddress = android.provider.Settings.Secure.getString(getContentResolver(), "bluetooth_address");
        Log.d(TAG, "### Mac ADD1 = " + macAddress);
        Log.d(TAG, "### UUID = " + UUID.randomUUID());
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            Log.d(TAG, "onBluetoothStateChanged = " + openOrClosed);
            if (openOrClosed) {
                CommonToast.toast(getApplicationContext(), "已开启");
            } else
                CommonToast.toast(getApplicationContext(), "已关闭");
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_center:
                startActivity(new Intent(BleDemoMainActivity.this, CenterActivity.class));
                break;
            case R.id.btn_device:
                if (!mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                    CommonToast.toast(getApplicationContext(), "设备不支持发广播");
                    return;
                }

                startActivity(new Intent(BleDemoMainActivity.this, PeripheralActivity.class));
                break;
        }
    }
}

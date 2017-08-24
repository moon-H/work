package com.cssweb.mytest.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by lenovo on 2017/4/7.
 */
public class DataContainer {
    public static HashMap<String, BluetoothDevice> mCenterConnectDevices = new HashMap<>();
    public static HashMap<String, BluetoothDevice> mPeripheralConnectDevices = new HashMap<>();
    public static int currentConnectDeviceNumber = -1;
    public static int myDeviceNumber = -1;
    public static ScanResult scanResult;
    public static final String ACTION_SEND_DATA_BY_CENTER = "com.cssweb.shankephone.ACTION_SEND_SERVICE_TO_CENTER";
    public static final String ACTION_SEND_DATA_BY_PERIPHERAL = "com.cssweb.shankephone.ACTION_SEND_DATA_BY_PERIPHERAL";

    public static final String KEY_RECEIVE_DATA = "key_receive_data";

    public static BluetoothDevice oldApiFindDevice;
    /**
     * 此设备是否连接了周边设备
     */
    public static boolean isPhriheralConnected(BluetoothDevice device) {
        for (String key : mPeripheralConnectDevices.keySet()) {
            if (key.equals(device.getAddress())) {
                return true;
            } else
                return false;
        }
        return false;
    }

    public static int getPeripheralConnectedSize() {
        return mPeripheralConnectDevices.size();
    }

    /**
     * 此设备是否被中心连接
     */
    public static boolean isCentrallConnected(BluetoothDevice device) {
        for (String key : mCenterConnectDevices.keySet()) {
            if (key.equals(device.getAddress())) {
                return true;
            } else
                return false;
        }
        return false;
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

}

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cssweb.mytest.ble.tool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cssweb.mytest.CommonToast;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.ble.BleMainActivity;
import com.cssweb.mytest.ble.DataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class CenterManager {
    private final static String TAG = CenterManager.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTTING";
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_CONNECT_TIME_OUT = "com.example.bluetooth.le.ACTION_GATT_CONNECT_TIME_OUT";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_STOP_SCAN = "com.example.bluetooth.le.ACTION_STOP_SCAN";
    public final static String ACTION_START_SCAN = "com.example.bluetooth.le.ACTION_START_SCAN";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_FIND_DEVICE = "com.example.bluetooth.le.ACTION_FIND_DEVICE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private Context mContext;

    CountDownTimer mConnectTimer;

    private BluetoothLeScanner mBluetoothLeScanner;
    BluetoothManager bluetoothManager;
    private boolean isConnecting;
    private BluetoothDevice mCurrentScanDevice;
    private int mTempDeviceNumber;

    public CenterManager(Context context) {
        mContext = context;
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothLeScanner = bluetoothManager.getAdapter().getBluetoothLeScanner();

        initialize();

        mConnectTimer = new CountDownTimer(10000, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mConnectionState == STATE_CONNECTED || mConnectionState == STATE_DISCONNECTED) {
                    mConnectTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "center连接超时");
                sendBleBroadCast(ACTION_GATT_CONNECT_TIME_OUT);
                isConnecting = false;
            }
        };
    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "### onConnectionStateChange status = " + status + " new status = " + newState);
            String intentAction;
            mConnectTimer.cancel();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                sendBleBroadCast(intentAction);
                DataContainer.mCenterConnectDevices.put(gatt.getDevice().getAddress(), gatt.getDevice());
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonToast.toast(mContext, "中心连接成功");

                    }
                });
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                DataContainer.currentConnectDeviceNumber = -1;
                DataContainer.mCenterConnectDevices.clear();
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.d(TAG, "Disconnected from GATT server.");
                sendBleBroadCast(intentAction);
                ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonToast.toast(mContext, "中心断开连接");
                    }
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                isConnecting = false;
                sendBleBroadCast(ACTION_GATT_SERVICES_DISCOVERED);
                DataContainer.currentConnectDeviceNumber = mTempDeviceNumber;

                //----liwx
                //                enableNotificationOfCharacteristic(true);
                //                enableNotification(gatt, HEART_RATE_SERVICE_UUID, HEART_RATE_CONTROL_POINT_UUID);
                //                writeData();
                //----liwx
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                sendBleBroadCast(ACTION_DATA_AVAILABLE, "");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged");
            String receiveData = HexConverter.bytesToHexString(characteristic.getValue());
            Log.d(TAG, "onDescriptorWrite1 = " + receiveData);
            sendBleBroadCast(DataContainer.ACTION_SEND_DATA_BY_CENTER, receiveData);
            sendBleBroadCast(ACTION_DATA_AVAILABLE, receiveData);
            ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonToast.toast(mContext, "中心收到消息了");
                }
            });
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite1 = " + HexConverter.bytesToHexString(descriptor.getCharacteristic().getValue()));
            Log.d(TAG, "onDescriptorWrite2 = " + HexConverter.bytesToHexString(descriptor.getValue()));
            Log.d(TAG, "onDescriptorWrite3 = " + status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorRead");
        }
    };

    private void sendBleBroadCast(final String action) {

        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        //        sendBroadcast(intent);
    }

    private void sendBleBroadCast(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public CenterManager getService() {
            return CenterManager.this;
        }
    }

    //    @Override
    //    public IBinder onBind(Intent intent) {
    //        Log.d(TAG, "onBind");
    //        return mBinder;
    //    }

    //    @Override
    //    public boolean onUnbind(Intent intent) {
    //        // After using a given device, you should make sure that BluetoothGatt.close() is called
    //        // such that resources are cleaned up properly.  In this particular example, close() is
    //        // invoked when the UI is disconnected from the Service.
    //        close();
    //        return super.onUnbind(intent);
    //    }

    //    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        //        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
        //            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
        //            if (mBluetoothGatt.connect()) {
        //                mConnectionState = STATE_CONNECTING;
        //                return true;
        //            } else {
        //                return false;
        //            }
        //        }
        isConnecting = true;
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            isConnecting = false;
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        sendBleBroadCast(ACTION_GATT_CONNECTTING);
        Log.d(TAG, "Trying to create a new connection.");
        mConnectTimer.start();
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled, byte[] value) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // This is specific to Heart Rate Measurement.
        //        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        Log.d(TAG, "### UUID = " + characteristic.getUuid());
        descriptor.setValue(value);
        final boolean result = mBluetoothGatt.writeDescriptor(descriptor);
        ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result)
                    CommonToast.toast(mContext, "发送消息成功");
                else
                    CommonToast.toast(mContext, "发送消息失败");

            }
        });
        //        }

    }

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    public void writeData() {
        Log.d(TAG, "####");
        BluetoothGattCharacteristic characteristic;
        //        mHeartRateControlPoint = new BluetoothGattCharacteristic(HEART_RATE_CONTROL_POINT_UUID, BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);
        //        mHeartRateControlPoint.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        BluetoothGattService service = mBluetoothGatt.getService(HEART_RATE_SERVICE_UUID);
        characteristic = service.getCharacteristic(HEART_RATE_MEASUREMENT_UUID);
        mBluetoothGatt.setCharacteristicNotification(characteristic, true);

        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            Log.d(TAG, "### trace1  =");
            mBluetoothGatt.setCharacteristicNotification(characteristic, false);
            mBluetoothGatt.readCharacteristic(characteristic);
        }
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            Log.d(TAG, "### trace2  =");
            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        }
        characteristic.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    private static final UUID HEART_RATE_MEASUREMENT_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");
    private static final UUID HEART_RATE_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");

    public void writeCustomData(byte[] data) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(HEART_RATE_SERVICE_UUID);
            if (service != null) {
                final BluetoothGattCharacteristic characteristic = service.getCharacteristic(HEART_RATE_MEASUREMENT_UUID);
                //                characteristic.setValue(HexConverter.hexStringToBytes(data));
                setCharacteristicNotification(characteristic, true, data);
            } else {
                Log.d(TAG, "service is null");
                disconnect();
            }
        } else {
            Log.d(TAG, "mBluetoothGatt is null");
            disconnect();
        }
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //            Log.d(TAG, " onScanResult " + result.toString());
            Map<ParcelUuid, byte[]> data = result.getScanRecord().getServiceData();
            String dataStr = HexConverter.bytesToHexString(data.get(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID)));
            mTempDeviceNumber = Integer.parseInt(dataStr, 16);
            Log.d(TAG, "扫描到的设备编号  =" + mTempDeviceNumber + " 已连接的设备编号 = " + DataContainer.currentConnectDeviceNumber);
            if (mTempDeviceNumber > DataContainer.myDeviceNumber) {
                if (DataContainer.currentConnectDeviceNumber == -1 || (DataContainer.currentConnectDeviceNumber != -1 && mTempDeviceNumber < DataContainer.currentConnectDeviceNumber)) {
                    //发现比当前已连接的设备编号小的设备，断开老的连接，连接新设备
                    disconnect();
                    sendBleBroadCast(ACTION_FIND_DEVICE);
                    mCurrentScanDevice = result.getDevice();
                    DataContainer.scanResult = result;
                } else if (DataContainer.currentConnectDeviceNumber != -1 && mTempDeviceNumber > DataContainer.currentConnectDeviceNumber) {
                    //扫描到的设备大于当前连接的设备，不做任何操作
                }

            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, " onBatchScanResults " + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, " onScanFailed " + errorCode);

        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startScanLeDevice(final boolean enable) {
        Log.d(TAG, "### start scan ");
        if (DataContainer.myDeviceNumber == -1) {
            ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonToast.toast(mContext, "请先设置设备编码");
                }
            });
            return;
        }
        isConnecting = false;
        disconnect();
        //        mLeDeviceListAdapter.clearAllDevice();
        //        mTvStatus.setText("扫描中....");
        sendBleBroadCast(ACTION_START_SCAN);
        //        UUID[] uuids = new UUID[]{BleMainActivity.HEART_RATE_SERVICE_UUID};
        List<ScanFilter> bleScanFilters = new ArrayList<>();
        bleScanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID)).build());

        ScanSettings bleScanSettings = new ScanSettings.Builder().build();
        mBluetoothLeScanner.startScan(bleScanFilters, bleScanSettings, mScanCallback);
    }   // Device scan callback.

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopScan() {
        Log.d(TAG, "stopScan");
        isConnecting = false;
        //        mTvStatus.setText("停止扫描....");
        sendBleBroadCast(ACTION_STOP_SCAN);
        mBluetoothLeScanner.stopScan(mScanCallback);
    }

    public BluetoothDevice getFindDevice() {
        return mCurrentScanDevice;
    }

    public int getConnectDeviceNumber() {
        return DataContainer.currentConnectDeviceNumber;
    }

}


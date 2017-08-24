package com.cssweb.mytest.ble.tool;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.ble.BleMainActivity;
import com.cssweb.mytest.ble.DataContainer;
import com.cssweb.mytest.ble.demo.BleDemoMainActivity;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by lenovo on 2017/4/26.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeripheralManager {
    public static final String TAG = "PeripheralManager";

    private BluetoothGattServer mGattServer;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattService mHeartRateService;
    private Context mContext;

    private BluetoothGattCharacteristic mHeartRateMeasurementCharacteristic;
    private BluetoothLeAdvertiser mAdvertiser;
    private AdvertiseSettings mAdvSettings;
    private AdvertiseData mAdvData;
    private AdvertiseData mAdvScanResponse;
    private BluetoothAdapter mBluetoothAdapter;

    private static final String HEART_RATE_MEASUREMENT_DESCRIPTION = "css test ";

    private static final UUID CHARACTERISTIC_USER_DESCRIPTION_UUID = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");

    public static final String ACTION_DEVICE_CONNECD = "com.cssweb.shankephone.ACTION_DEVICE_CONNECD";
    public static final String ACTION_DEVICE_DISCONNECT = "com.cssweb.shankephone.ACTION_DEVICE_DISCONNECT";
    public static final String ACTION_ADVERTISE_SUCCESS = "com.cssweb.shankephone.ACTION_ADVERTISE_SUCCESS";
    public static final String ACTION_ADVERTISE_FAILED = "com.cssweb.shankephone.ACTION_ADVERTISE_FAILED";
    public static final String ACTION_ADVERTISE_START = "com.cssweb.shankephone.ACTION_ADVERTISE_START";

    Handler mHandler = new Handler();

    public PeripheralManager(Context context) {
        mContext = context;
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startAdvertise(byte[] advData) {
        initAdvtise(advData);
        Log.d(TAG, "开始广播----------------");
        sendBleBroadCast(ACTION_ADVERTISE_START);
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
        mGattServer.addService(mHeartRateService);
        if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
            mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
            mAdvertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, mAdvCallback);
        }
    }

    //初始化一个服务
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAdvtise(byte[] advData) {
        mAdvSettings = new AdvertiseSettings.Builder().
            setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY).
            setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH).setConnectable(true).build();
        //        byte[] data = HexConverter.hexStringToBytes(Integer.toHexString(Integer.parseInt(mEditText.getText().toString())));
        mAdvData = new AdvertiseData.Builder().
            setIncludeTxPowerLevel(true).
            addServiceUuid(new ParcelUuid(BleDemoMainActivity.CUSTOM_UUID)).
            addServiceData(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID), advData).build();
        mAdvScanResponse = new AdvertiseData.Builder().setIncludeDeviceName(true).build();


        mHeartRateService = new BluetoothGattService(BleMainActivity.HEART_RATE_SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        mHeartRateMeasurementCharacteristic = new BluetoothGattCharacteristic(BleMainActivity.HEART_RATE_MEASUREMENT_UUID, BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            /* No permissions */ BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ);

        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(BleMainActivity.CLIENT_CHARACTERISTIC_CONFIGURATION_UUID, (BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE));
        descriptor.setValue(new byte[]{0, 0});

        BluetoothGattDescriptor descriptor2 = new BluetoothGattDescriptor(CHARACTERISTIC_USER_DESCRIPTION_UUID, (BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE));


        try {
            descriptor2.setValue(HEART_RATE_MEASUREMENT_DESCRIPTION.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mHeartRateMeasurementCharacteristic.addDescriptor(descriptor);
        mHeartRateMeasurementCharacteristic.addDescriptor(descriptor2);

        mHeartRateService.addCharacteristic(mHeartRateMeasurementCharacteristic);

    }

    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d(TAG, "device name = " + device.getName() + " uuid = " + device.getUuids() + " status = " + status + " new state = " + newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    ParcelUuid[] parcelUuids = device.getUuids();
                    Log.d(TAG, "Connected to device: " + device.getAddress());
                    Log.d(TAG, "Connected to device: " + device.getUuids());
                    //                    if (DataContainer.isCentrallConnected(device) || DataContainer.getPeripheralConnectedSize() > 0) {
                    //                        mGattServer.cancelConnection(device);
                    //                        //                        ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
                    //                        //                            @Override
                    //                        //                            public void run() {
                    //                        //                                CommonToast.toast(getActivity(), "此设备已经连接");
                    //                        //                            }
                    //                        //                        });
                    //                    } else {
                    DataContainer.mPeripheralConnectDevices.put(device.getAddress(), device);
                    sendBleBroadCast(ACTION_DEVICE_CONNECD);
                    //                    }
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.d(TAG, "Disconnected from device");
                    DataContainer.mPeripheralConnectDevices.remove(device.getAddress());
                    sendBleBroadCast(ACTION_DEVICE_DISCONNECT);
                }
            } else {
                Log.e(TAG, "Error when connecting: " + status);
            }
        }

        @Override
        public void onDescriptorWriteRequest(final BluetoothDevice device, final int requestId, final BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, final byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            sendBleBroadCast(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL, HexConverter.bytesToHexString(value));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "收到消息", Toast.LENGTH_SHORT).show();
                }

            });
            if (responseNeeded) {
                Log.d(TAG, "## sendResponse");
                //                mHandler.post(new Runnable() {
                //                    @Override
                //                    public void run() {
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 2, HexConverter.hexStringToBytes("1315"));
                //                    }
                //                });
            }
            notifyData();
        }
    };
    private final AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            //            mTvAdvStatus.setText("广播失败");
            sendBleBroadCast(ACTION_ADVERTISE_FAILED);
            Log.e(TAG, "Not broadcasting: " + errorCode);
            int statusText;
            switch (errorCode) {
                case ADVERTISE_FAILED_ALREADY_STARTED:
                    //                    statusText = R.string.status_advertising;
                    Log.w(TAG, "App was already advertising");
                    break;
                case ADVERTISE_FAILED_DATA_TOO_LARGE:
                    //                    statusText = R.string.status_advDataTooLarge;
                    Log.w(TAG, "status_advDataTooLarge");
                    break;
                case ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                    //                    statusText = R.string.status_advFeatureUnsupported;
                    break;
                case ADVERTISE_FAILED_INTERNAL_ERROR:
                    //                    statusText = R.string.status_advInternalError;
                    break;
                case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                    //                    statusText = R.string.status_advTooManyAdvertisers;
                    break;
                default:
                    //                    statusText = R.string.status_notAdvertising;
                    Log.wtf(TAG, "Unhandled error: " + errorCode);
            }
            //            mAdvStatus.setText(statusText);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            //            mTvAdvStatus.setText("广播中...");
            sendBleBroadCast(ACTION_ADVERTISE_SUCCESS);
            Log.d(TAG, "## Broadcasting");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //            Log.d(TAG, "### AdvertiseSettings = " + settingsInEffect.toString());
            //            mAdvStatus.setText(R.string.status_advertising);
        }
    };

    private void sendBleBroadCast(String action) {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void sendBleBroadCast(String action, String data) {
        Intent intent = new Intent(action);
        intent.putExtra("value", data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public void notifyData() {
        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
            // true for indication (acknowledge) and false for notification (unacknowledge).
            //            Log.d(TAG, "##### NotificationToDevices = " + indicate);
            mHeartRateMeasurementCharacteristic.setValue(new byte[]{(byte) 0x43});
            boolean result = mGattServer.notifyCharacteristicChanged(DataContainer.mPeripheralConnectDevices.get(key), mHeartRateMeasurementCharacteristic, false);
            Log.d(TAG, "##### NotificationToDevices result= " + result);
        }
    }

    public void stopAdvertise() {
        Log.d(TAG, "stopAdvertise");
        if (mAdvertiser != null) {
            mAdvertiser.stopAdvertising(mAdvCallback);
        }
    }


}

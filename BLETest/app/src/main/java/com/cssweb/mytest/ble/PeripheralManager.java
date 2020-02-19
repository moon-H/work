package com.cssweb.mytest.ble;

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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cssweb.mytest.Common;
import com.cssweb.mytest.utils.HexConverter;

import java.util.UUID;


/**
 * Created by lenovo on 2017/4/26.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeripheralManager {

    private static final int STOP_ADVERTISE_DELAY_TIME = 15 * 1000;//停止广播时间

    public static final String TAG = "PeripheralManager";

    private BluetoothGattServer mGattServer;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattService mDataShareService;
    //    private BluetoothGattService mTransactionService;
    private Context mContext;

    private BluetoothGattCharacteristic mDataShareCharacteristic;
    //    private BluetoothGattCharacteristic mTransactionCharacteristic;
    private BluetoothLeAdvertiser mAdvertiser;
    private AdvertiseSettings mAdvSettings;
    private AdvertiseData mAdvDataDeviceCode;//发送设备编号用
    //    private AdvertiseData mAdvDataTransaction;//发送交易数据用
    private AdvertiseData mAdvScanResponse;
    private BluetoothAdapter mBluetoothAdapter;

    Handler mHandler = new Handler();
    private OnPeripheralCallback mCallback;
    private String mTempAdvData;
    private CountDownTimer mStopAdvertiseTimer;

    public PeripheralManager(Context context) {
        try {
            mContext = context;
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        } catch (Exception e) {
            Log.d(TAG, "PeripheralManager error ", e);
        }
        initStopAdvertiseTimer();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startAdvertise(UUID serviceUUID, UUID dataUUID, byte[] advData1) {
        try {
            Log.d(TAG, "开始广播 数据长度---" + advData1.length + " 内容 = " + HexConverter.bytesToHexString(advData1));
            stopAdvertiseTimer();
            startAdvertiseTimer();//每个广播有效期15s

            mTempAdvData = HexConverter.bytesToHexString(advData1);
            initAdvertise(serviceUUID, dataUUID, advData1);
            //        sendBleBroadCast(ACTION_ADVERTISE_START);
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mGattServer == null) {
                initService(serviceUUID, dataUUID);
                //            Log.d(TAG, "mGattServer == null");
                mGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
                mGattServer.addService(mDataShareService);
            }
            //        mGattServer.addService(mTransactionService);
            if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                mAdvertiser.startAdvertising(mAdvSettings, mAdvDataDeviceCode, mAdvScanResponse, mAdvCallback);
                //            mAdvertiser.startAdvertising(mAdvSettings, mAdvDataTransaction, mAdvScanResponse, mAdvCallback);
            }
        } catch (Exception e) {
            Log.d(TAG, "startAdvertise occur exception ", e);
            if (mCallback != null)
                mCallback.onAdvertiseFailed(-99);
        }
    }

    /**
     * 初始化服务。
     *
     * @param serviceUUID 区分进出站的UUID
     * @param dataUUID    PAD互联时传输数据的UUID
     * @param advData1    广播数据 设备编号和交易数据
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAdvertise(UUID serviceUUID, UUID dataUUID, byte[] advData1) {
        try {
            mAdvDataDeviceCode = new AdvertiseData.Builder().
                setIncludeDeviceName(false).
                setIncludeTxPowerLevel(true).
                addServiceUuid(new ParcelUuid(serviceUUID)).
                addServiceData(new ParcelUuid(dataUUID), advData1).build();
        } catch (Exception e) {
            Log.d(TAG, " initAdvertise occur error: ", e);
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initService(UUID serviceUUID, UUID dataUUID) {
        try {
            mAdvSettings = new AdvertiseSettings.Builder().
                setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY).
                setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH).setConnectable(true).build();
            //        byte[] data = HexConverter.hexStringToBytes(Integer.toHexString(Integer.parseInt(mEditText.getText().toString())));
            mAdvScanResponse = new AdvertiseData.Builder().setIncludeDeviceName(false).build();
            //--------service1 start-------------
            mDataShareService = new BluetoothGattService(serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);
            mDataShareCharacteristic = new BluetoothGattCharacteristic(Common.UUID_CHARACTERISTIC_DATA_SHARE, BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            /* No permissions */ BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY);
            BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(Common.UUID_CHARACTERISTIC_DATA_SHARE, (BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY));
            descriptor.setValue(new byte[]{0, 0});
            mDataShareCharacteristic.addDescriptor(descriptor);
            mDataShareService.addCharacteristic(mDataShareCharacteristic);
        } catch (Exception e) {
            Log.d(TAG, "initService occur an error ", e);
        }
        //--------service1 end-------------

        //--------service2 start-------------
        //        mTransactionService = new BluetoothGattService(Common.UUID_SERVICE_TRANSACTION, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //        mTransactionCharacteristic = new BluetoothGattCharacteristic(Common.UUID_CHARACTERISTIC_TRANSACTION, BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
        //                    /* No permissions */ BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY);

        //        BluetoothGattDescriptor descriptorTransaction = new BluetoothGattDescriptor(Common.UUID_CHARACTERISTIC_TRANSACTION, (BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY));
        //        descriptorTransaction.setValue(new byte[]{0, 0});
        //        mTransactionCharacteristic.addDescriptor(descriptorTransaction);
        //        mTransactionService.addCharacteristic(mTransactionCharacteristic);
        //--------service2 end-------------

    }


    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d(TAG, "device name = " + device.getName() + " status = " + status + " new state = " + newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    if (mCallback != null)
                        mCallback.onGattConnectSuccess();
                    //                    ParcelUuid[] parcelUuids = device.getUuids();
                    //                    Log.d(TAG, "Connected to device: " + device.getAddress());
                    //                    Log.d(TAG, "Connected to device: " + device.getUuids());
                    //                    sendBleBroadCast(ACTION_DEVICE_CONNECD);
                    //                    }
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.d(TAG, "Disconnected from device = " + device.getAddress());
                    if (mCallback != null)
                        mCallback.onGattDisConnect(newState);
                    //                    sendBleBroadCast(ACTION_DEVICE_DISCONNECT);
                }
            } else {
                Log.e(TAG, "Error when connecting: " + status);
                if (mCallback != null)
                    mCallback.onGattDisConnect(status);
            }
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            Log.d(TAG, "onDescriptorReadRequest  device = " + device.getName() + " requestId = " + requestId + " offset =" + offset + " UUID = " + descriptor.getUuid().toString());
            if (descriptor.getValue() != null)
                Log.d(TAG, "onDescriptorReadRequest = " + HexConverter.bytesToHexString(descriptor.getValue()));
            else {
                Log.d(TAG, "onDescriptorReadRequest data is null ");
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.d(TAG, "onCharacteristicReadRequest  device = " + device.getName() + " requestId = " + requestId + " offset =" + offset + " UUID = " + characteristic.getUuid().toString());
            if (characteristic.getValue() != null)
                Log.d(TAG, "onCharacteristicReadRequest = " + HexConverter.bytesToHexString(characteristic.getValue()));
            else {
                Log.d(TAG, "onCharacteristicReadRequest data is null ");
            }
            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, HexConverter.hexStringToBytes("1315"));
            if (mCallback != null) {
                mCallback.onCharacteristicReadRequest();
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            String hexValue = HexConverter.bytesToHexString(value);
            Log.d(TAG, "onCharacteristicWriteRequest  value = " + hexValue);
        }

        @Override
        public void onDescriptorWriteRequest(final BluetoothDevice device, final int requestId, final BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, final byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            //周边收到消息
            Log.d(TAG, "onDescriptorWriteRequest  device = " + device.getName() + " requestId =" + requestId + " descriptor UUID = " + descriptor.getUuid().toString());
            Log.d(TAG, "onDescriptorWriteRequest  preparedWrite = " + preparedWrite + " responseNeeded =" + responseNeeded + " offset = " + offset);
            String hexValue = HexConverter.bytesToHexString(value);
            Log.d(TAG, "onDescriptorWriteRequest  value = " + hexValue);

            if (mCallback != null) {
                mCallback.onPeripheralReceiveData(value);
            }

            //            sendBleBroadCast(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL, HexConverter.bytesToHexString(value));
            //            mHandler.post(new Runnable() {
            //                @Override
            //                public void run() {
            //                    Toast.makeText(mContext, "收到消息", Toast.LENGTH_SHORT).show();
            //                }
            //
            //            });
            if (responseNeeded) {
                Log.d(TAG, "## sendResponse");
                //                mHandler.post(new Runnable() {
                //                    @Override
                //                    public void run() {
                try {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 2, HexConverter.hexStringToBytes("1315"));
                } catch (Exception e) {
                    Log.d(TAG, " sendResponse occur  error :", e);
                }
                //                    }
                //                });
            }
            //            notifyData();
            //            notifyData(HexConverter.hexStringToBytes("9988776655"));
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
            Log.d(TAG, "onNotificationSent  device = " + device.getAddress() + " status =" + status);

        }
    };
    private final AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            //            mTvAdvStatus.setText("广播失败");
            Log.e(TAG, "broadcast failed: " + errorCode);
            if (mCallback != null)
                mCallback.onAdvertiseFailed(errorCode);
            //            sendBleBroadCast(ACTION_ADVERTISE_FAILED);
            int statusText;
            switch (errorCode) {
                case ADVERTISE_FAILED_ALREADY_STARTED:
                    //                    statusText = R.string.status_advertising;
                    Log.d(TAG, "App was already advertising");
                    break;
                case ADVERTISE_FAILED_DATA_TOO_LARGE:
                    //                    statusText = R.string.status_advDataTooLarge;
                    Log.d(TAG, "status_advDataTooLarge");
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
                    Log.d(TAG, "Unhandled error: " + errorCode);
            }
            //            mAdvStatus.setText(statusText);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "## Broadcasting");
            if (mCallback != null)
                mCallback.onAdvertiseSuccess(mTempAdvData);
            //            sendBleBroadCast(ACTION_ADVERTISE_SUCCESS);
            //            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

    /**
     * 数据同步时使用此方法将数据同步给其他pad
     */
//    public void notifyData(byte[] data) {
//        Log.d(TAG, "notifyData= " + HexConverter.bytesToHexString(data));
//        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
//            if (mGattServer != null && mDataShareCharacteristic != null) {
//                mDataShareCharacteristic.setValue(data);
//                boolean result = mGattServer.notifyCharacteristicChanged(DataContainer.mPeripheralConnectDevices.get(key), mDataShareCharacteristic, true);
//                Log.d(TAG, "##### NotificationToDevices result= " + result + " deviceAddress = " + key);
//            } else {
//                Log.d(TAG, "notify data failed");
//            }
//        }
//    }

    /**
     * 数据同步时使用此方法将数据同步给其他pad
     */
    //    public void notifyPhoneData(byte[] data) {
    //        Log.d(TAG, "notifyPhoneData= " + HexConverter.bytesToHexString(data));
    //        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
    //            mTransactionCharacteristic.setValue(data);
    //            boolean result = mGattServer.notifyCharacteristicChanged(DataContainer.mPeripheralConnectDevices.get(key), mTransactionCharacteristic, true);
    //            Log.d(TAG, "##### notifyPhoneData result= " + result + " deviceAddress = " + key);
    //        }
    //    }
    public void stopAdvertise() {
        Log.d(TAG, "stopAdvertise");
        if (!isBluetoothOpened()) {
            Log.d(TAG, "bluetooth not open");
            return;
        }
        try {
            if (mAdvertiser != null) {
                mAdvertiser.stopAdvertising(mAdvCallback);
            }
            //            if (mGattServer != null) {dxsdfa ddd
            //                mGattServer.clearServices();
            //            }
        } catch (Exception e) {
            Log.d(TAG, "stopAdvertise occur error: ", e);
        }
    }

    public void closeGattServer() {
        Log.d(TAG, "closeGattServer");
        if (!isBluetoothOpened()) {
            Log.d(TAG, "bluetooth not open");
            return;
        }
        try {
            if (mGattServer != null) {
                mGattServer.close();
                mGattServer.clearServices();
            }
        } catch (Exception e) {
            Log.d(TAG, "closeGattServer occur error :", e);
        }
    }

    public void setOnPeripheralCallback(OnPeripheralCallback callback) {
        mCallback = callback;
    }

    public interface OnPeripheralCallback {
        void onAdvertiseSuccess(String data);

        void onAdvertiseFailed(int code);

        void onGattConnectSuccess();

        void onGattDisConnect(int code);

        void onPeripheralReceiveData(byte[] data);

        void onCharacteristicReadRequest();
    }

    private boolean isBluetoothOpened() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            return true;
        }
        return false;
    }

    /**
     * 初始化停止广播定时器
     */
    private void initStopAdvertiseTimer() {
        mStopAdvertiseTimer = new CountDownTimer(STOP_ADVERTISE_DELAY_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "stopAdvertiseTimer onTick####");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "stopAdvertiseTimer 停止广播");
                //                setScreenOff();
                stopAdvertise();
            }
        };
    }

    /**
     * 启动 停止广播定时器
     */
    private void startAdvertiseTimer() {
        if (mStopAdvertiseTimer != null) {
            mStopAdvertiseTimer.start();
        }
    }

    /**
     * 关闭 停止广播定时器
     */
    private void stopAdvertiseTimer() {
        if (mStopAdvertiseTimer != null) {
            mStopAdvertiseTimer.cancel();
        }
    }
}

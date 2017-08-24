package com.cssweb.mytest.ble;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cssweb.mytest.CommonToast;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.utils.ByteConverter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by lenovo on 2017/4/6.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeripheralFragment extends Fragment {
    private static final String TAG = "PeripheralFragment";
    private View mRootView;
    //----ble周边设备------------
    private BluetoothGattServer mGattServer;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattService mHeartRateService;

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothGattCharacteristic mHeartRateMeasurementCharacteristic;
    private BluetoothLeAdvertiser mAdvertiser;
    private AdvertiseSettings mAdvSettings;
    private AdvertiseData mAdvData;
    private AdvertiseData mAdvScanResponse;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    //----------------
    private TextView mTvConnectedDevice;
    private TextView mTvAdvStatus;
    private TextView mTvReceiveData;
    private EditText mEditText;

    private static final String HEART_RATE_MEASUREMENT_DESCRIPTION = "Used to send a heart rate " + "measurement";

    private static final UUID CHARACTERISTIC_USER_DESCRIPTION_UUID = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");

    private static final String ACTION_DEVICE_CONNECD = "com.cssweb.shankephone.ACTION_DEVICE_CONNECD";
    private static final String ACTION_DEVICE_DISCONNECD = "com.cssweb.shankephone.ACTION_DEVICE_DISCONNECD";
    private static final String ACTION_DEVICE_ALREADY_CONNECT = "com.cssweb.shankephone.ACTION_DEVICE_ALREADY_CONNECT";

    //    private HashMap<String, BluetoothDevice> mConnectedDeviceMap = new HashMap<>();

    public static PeripheralFragment getInstance() {
        return new PeripheralFragment();
    }

    private Timer mStatusTimer = new Timer();
    private static final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DEVICE_CONNECD);
        filter.addAction(ACTION_DEVICE_DISCONNECD);
        filter.addAction(DataContainer.ACTION_SEND_DATA_BY_CENTER);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mRootView == null) {
            Log.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_peripheral, container, false);
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();

            mRootView.findViewById(R.id.btn_start_adv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAdvertise();
                }
            });
            mRootView.findViewById(R.id.btn_stop_adv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopAdvertise();
                }
            });
            mTvConnectedDevice = (TextView) mRootView.findViewById(R.id.tv_connected_device);
            mTvAdvStatus = (TextView) mRootView.findViewById(R.id.tv_status);
            mRootView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyData();
                }
            });

            mRootView.findViewById(R.id.btn_cancel_connect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelDeviceConnect();
                }
            });

            mEditText = (EditText) mRootView.findViewById(R.id.edt_deviceNumber);
            mTvReceiveData = (TextView) mRootView.findViewById(R.id.tv_receive);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void ensureBleFeaturesAvailable() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "不支持蓝牙", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Bluetooth not supported");
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Make sure bluetooth is enabled.
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    //初始化一个服务
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initPeripheral() {
        mAdvSettings = new AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED).setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM).setConnectable(true).build();
        //        byte[] data = HexConverter.hexStringToBytes(Integer.toHexString(Integer.parseInt(mEditText.getText().toString())));
        String hexData = HexConverter.bytesToHexString(ByteConverter.intToByte(Integer.valueOf(mEditText.getText().toString().trim())));
        byte[] byteData = HexConverter.hexStringToBytes(hexData);
        mAdvData = new AdvertiseData.Builder().setIncludeTxPowerLevel(true).addServiceUuid(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID)).addServiceData(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID), byteData).build();
        int myDeviceNumber = Integer.valueOf(mEditText.getText().toString().trim());
        DataContainer.myDeviceNumber = myDeviceNumber;
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

    private TimerTask advTask;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startAdvertise() {
        if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
            CommonToast.toast(getActivity(), "必须设置设备编号");
            return;
        }
        cancelDeviceConnect();
        initPeripheral();
        mTvAdvStatus.setText("开始广播");
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
        mGattServer.addService(mHeartRateService);
        if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
            Log.d(TAG, "### startAdvertising");
            mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
            Log.d(TAG, "##### address = " + mBluetoothAdapter.getAddress());
            mAdvertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, mAdvCallback);
            advTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            stopAdvertise();
                            startAdvertise();
                            //mAdvertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, mAdvCallback);
                        }
                    });
                }
            };
            if (!hasStartTimer) {
                hasStartTimer = true;
                mStatusTimer.schedule(advTask, 1000, 30 * 1000);
            }
        }
    }

    boolean hasStartTimer = false;


    private final AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            mTvAdvStatus.setText("广播失败");
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
            mTvAdvStatus.setText("广播中...");
            Log.d(TAG, "## Broadcasting");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //            Log.d(TAG, "### AdvertiseSettings = " + settingsInEffect.toString());
            //            mAdvStatus.setText(R.string.status_advertising);
        }
    };


    private void stopAdvertise() {
        Log.d(TAG, "stop advertise");
        if (mGattServer != null) {
            mGattServer.close();
        }
        if (mBluetoothAdapter.isEnabled() && mAdvertiser != null) {
            // If stopAdvertising() gets called before close() a null
            // pointer exception is raised.
            mAdvertiser.stopAdvertising(mAdvCallback);
            mTvAdvStatus.setText("停止广播");
        }
    }


    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d(TAG, "device name = " + device.getName() + " uuid = " + device.getUuids() + " status = " + status + " new state = " + newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.d(TAG, "Connected to device: " + device.getAddress());
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
                    sendBleBroadCast(ACTION_DEVICE_DISCONNECD);
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
                    Toast.makeText(getActivity(), "收到消息", Toast.LENGTH_SHORT).show();
                    mTvReceiveData.setText("收到消息：" + dateFomatter.format(System.currentTimeMillis()) + "  内容：" + HexConverter.bytesToHexString(value));
                }

            });
            if (responseNeeded) {
                Log.d(TAG, "## sendResponse");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 2, HexConverter.hexStringToBytes("1315"));
                    }
                }, 1000);
            }
        }
    };
    Handler mHandler = new Handler();

    private void sendBleBroadCast(String action) {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void sendBleBroadCast(String action, String data) {
        Intent intent = new Intent(action);
        intent.putExtra("value", data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_DEVICE_CONNECD)) {
                    updateConnectedDeviceInfo();
                    //                    Toast.makeText(getActivity(), "周边设备连接成功。。", Toast.LENGTH_SHORT).show();

                } else if (action.equals(ACTION_DEVICE_DISCONNECD)) {
                    updateConnectedDeviceInfo();
                    //                    Toast.makeText(getActivity(), "周边设备断开连接。。", Toast.LENGTH_SHORT).show();
                } else if (action.equals(DataContainer.ACTION_SEND_DATA_BY_CENTER)) {
                    String newData = intent.getStringExtra("value");

                    String localdata = DataContainer.getString(mContext, DataContainer.KEY_RECEIVE_DATA);
                    if (TextUtils.isEmpty(localdata)) {
                        Log.d(TAG, "本地空通知给其他设备");
                        DataContainer.putString(mContext, DataContainer.KEY_RECEIVE_DATA, newData);
                        notifyData();
                    } else if (!TextUtils.isEmpty(localdata) && localdata.equals(newData)) {
                        CommonToast.toast(mContext, "收到的消息本地已经有了");
                    } else if (!TextUtils.isEmpty(localdata) && !localdata.equals(newData)) {
                        Log.d(TAG, "本地不同通知给其他设备");
                        DataContainer.putString(mContext, DataContainer.KEY_RECEIVE_DATA, newData);
                        notifyData();
                    }
                    mTvReceiveData.setText("收到中心消息：" + dateFomatter.format(System.currentTimeMillis()) + "   " + newData);
                }
            }
        }
    };

    private void updateConnectedDeviceInfo() {
        StringBuilder buffer = new StringBuilder();
        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
            BluetoothDevice device = DataContainer.mPeripheralConnectDevices.get(key);
            buffer.append(device.getName()).append(" ").append(device.getAddress()).append("\n");
        }
        mTvConnectedDevice.setText("连接的设备：" + buffer.toString());
    }

    private void notifyData() {
        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
            // true for indication (acknowledge) and false for notification (unacknowledge).
            //            Log.d(TAG, "##### NotificationToDevices = " + indicate);
            mHeartRateMeasurementCharacteristic.setValue(new byte[]{(byte) 0x13});
            boolean result = mGattServer.notifyCharacteristicChanged(DataContainer.mPeripheralConnectDevices.get(key), mHeartRateMeasurementCharacteristic, false);
            Log.d(TAG, "##### NotificationToDevices result= " + result);
        }
    }

    private void cancelDeviceConnect() {
        for (String key : DataContainer.mPeripheralConnectDevices.keySet()) {
            Log.d(TAG, "key = " + key + "name = " + DataContainer.mPeripheralConnectDevices.get(key).getName());
            BluetoothDevice device = DataContainer.mPeripheralConnectDevices.get(key);
            mGattServer.cancelConnection(device);
        }
    }

}

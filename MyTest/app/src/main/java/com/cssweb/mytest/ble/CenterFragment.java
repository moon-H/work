package com.cssweb.mytest.ble;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cssweb.mytest.CommonToast;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.ble.tool.CenterManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo on 2017/4/6.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CenterFragment extends Fragment {
    private static final String TAG = "CenterFragment";
    private View mRootView;
    private Context mContext;
    private CenterManager mCenterManager;
    private String mDeviceAddress;
    private BluetoothLeScanner mBluetoothLeScanner;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    BluetoothManager bluetoothManager;
    private TextView mTvStatus;
    //    private EditText mEditText;
    private TextView mReceiveData;
    private TextView mCurrentTime;
    private TextView mSendTime;
    private EditText mEditText;
    private static final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());

    public static CenterFragment getInstance() {
        return new CenterFragment();
    }

    private Timer timer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CenterManager.ACTION_GATT_CONNECTTING);
        filter.addAction(CenterManager.ACTION_GATT_CONNECTED);
        filter.addAction(CenterManager.ACTION_GATT_DISCONNECTED);
        filter.addAction(CenterManager.ACTION_GATT_CONNECT_TIME_OUT);
        filter.addAction(CenterManager.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(CenterManager.ACTION_FIND_DEVICE);
        filter.addAction(CenterManager.ACTION_START_SCAN);
        filter.addAction(CenterManager.ACTION_STOP_SCAN);
        filter.addAction(CenterManager.ACTION_DATA_AVAILABLE);
        filter.addAction(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
        String hexValue = HexConverter.bytesToHexString(new byte[]{0x0F});
        Log.d(TAG, " value = " + Integer.parseInt(hexValue, 16));

    }

    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String currentTime = getCurrentDateTime();
                    //                    String date = currentTime.substring(0, 10);
                    //                    String time = currentTime.substring(10, currentTime.length());
                    mCurrentTime.setText(currentTime);
                    break;
            }
        }
    };

    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd    HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mRootView == null) {
            Log.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_center, container, false);
            ListView listView = (ListView) mRootView.findViewById(R.id.lv_device);
            mLeDeviceListAdapter = new LeDeviceListAdapter();
            listView.setAdapter(mLeDeviceListAdapter);

            mTvStatus = (TextView) mRootView.findViewById(R.id.tv_connected_device);
            //            mBluetoothAdapter.setName("hahahah");

            mRootView.findViewById(R.id.btn_stop_scan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCenterManager.stopScan();
                }
            });
            mRootView.findViewById(R.id.btn_start_scan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCenterManager.startScanLeDevice(true);
                }
            });
            mRootView.findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    List<BluetoothDevice> list = bluetoothManager.getConnectedDevices(BluetoothGattServer.GATT);
                    //                    Toast.makeText(getActivity(), "连接设备数量 = " + list.size(), Toast.LENGTH_SHORT).show();
                    mCenterManager.disconnect();
                }
            });
            //            mEditText = (EditText) mRootView.findViewById(R.id.edt);
            mRootView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
                                                                         @Override
                                                                         public void onClick(View v) {
                                                                             try {
                                                                                 byte[] value = HexConverter.hexStringToBytes(mEditText.getText().toString().trim());
                                                                                 mCenterManager.writeCustomData(value);
                                                                                 mSendTime.setText("发送消息时间： " + dateFomatter.format(System.currentTimeMillis()) + " " + mEditText.getText().toString().trim());
                                                                             } catch (Exception e) {
                                                                                 CommonToast.toast(mContext, "检查消息格式");
                                                                             }
                                                                         }
                                                                     }

            );

            mSendTime = (TextView) mRootView.findViewById(R.id.tv_send_time);
            mEditText = (EditText) mRootView.findViewById(R.id.edt_value);

            mReceiveData = (TextView) mRootView.findViewById(R.id.tv_receive_data);
            mCurrentTime = (TextView) mRootView.findViewById(R.id.tv_current_time);
            timer = new

                Timer(true);

            timer.schedule(task, 0, 1000);
        }
        mCenterManager = new CenterManager(mContext);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }


    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<ScanResult> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<ScanResult>();
            mInflator = ((BleMainActivity) mContext).getLayoutInflater();
        }

        public void addDevice(ScanResult device) {
            mLeDevices.clear();
            mLeDevices.add(device);
            //            if (!mLeDevices.contains(device)) {
            //                mLeDevices.add(device);
            //            }
        }

        public void clearAllDevice() {
            mLeDevices.clear();
            notifyDataSetChanged();
        }


        public ScanResult getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i).getDevice();
            Map<ParcelUuid, byte[]> data = mLeDevices.get(i).getScanRecord().getServiceData();

            String dataStr = HexConverter.bytesToHexString(data.get(new ParcelUuid(BleMainActivity.HEART_RATE_SERVICE_UUID)));


            final String deviceName = device.getName() + "   设备编号  = " + Integer.parseInt(dataStr, 16);
            if (deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(CenterManager.ACTION_GATT_CONNECTTING)) {
                    mTvStatus.setText("正在连接....");
                } else if (action.equals(CenterManager.ACTION_GATT_CONNECTED)) {
                    //                    mCenterManager.stopScan();
                    mTvStatus.setText("连接成功 查找服务....");
                } else if (action.equals(CenterManager.ACTION_GATT_SERVICES_DISCOVERED)) {
                    mTvStatus.setText("发现服务..建立连接完成.." + mCenterManager.getConnectDeviceNumber());
                } else if (action.equals(CenterManager.ACTION_GATT_CONNECT_TIME_OUT)) {
                    mTvStatus.setText("连接超时....");
                    mCenterManager.startScanLeDevice(true);
                } else if (action.equals(CenterManager.ACTION_GATT_DISCONNECTED)) {
                    mTvStatus.setText("连接失败....");
                    mCenterManager.startScanLeDevice(true);
                } else if (action.equals(CenterManager.ACTION_START_SCAN)) {
                    mTvStatus.setText("扫描中....");
                } else if (action.equals(CenterManager.ACTION_START_SCAN)) {
                    mTvStatus.setText("停止扫描....");
                } else if (action.equals(CenterManager.ACTION_FIND_DEVICE)) {
                    mTvStatus.setText("发现设备....");
                    mLeDeviceListAdapter.clearAllDevice();
                    ScanResult device = DataContainer.scanResult;
                    if (device != null) {
                        mLeDeviceListAdapter.addDevice(device);
                        mCenterManager.connect(device.getDevice().getAddress());
                    }
                } else if (action.equals(CenterManager.ACTION_DATA_AVAILABLE)) {
                    String value = intent.getStringExtra("value");
                    mReceiveData.setText("收到消息：" + dateFomatter.format(System.currentTimeMillis()) + "  内容：" + value);
                } else if (action.equals(DataContainer.ACTION_SEND_DATA_BY_PERIPHERAL)) {
                    String newData = intent.getStringExtra("value");
                    Log.d(TAG, "value = " + newData);
                    String localdata = DataContainer.getString(mContext, DataContainer.KEY_RECEIVE_DATA);
                    if (TextUtils.isEmpty(localdata)) {
                        Log.d(TAG, "本地空通知给其他设备");
                        DataContainer.putString(mContext, DataContainer.KEY_RECEIVE_DATA, newData);
                        mCenterManager.writeCustomData(HexConverter.hexStringToBytes(newData));
                    } else if (!TextUtils.isEmpty(localdata) && localdata.equals(newData)) {
                        CommonToast.toast(mContext, "收到的消息本地已经有了");
                    } else if (!TextUtils.isEmpty(localdata) && !localdata.equals(newData)) {
                        Log.d(TAG, "本地不同通知给其他设备");
                        DataContainer.putString(mContext, DataContainer.KEY_RECEIVE_DATA, newData);
                        mCenterManager.writeCustomData(HexConverter.hexStringToBytes(newData));
                    }
                    mReceiveData.setText("收到周边消息：" + dateFomatter.format(System.currentTimeMillis()) + "   " + newData);

                }
            }
        }
    };

    //    private void stopService() {
    //        Intent gattServiceIntent = new Intent(mContext, CenterManager.class);
    //
    //        mContext.stopService(gattServiceIntent);
    //    }


}

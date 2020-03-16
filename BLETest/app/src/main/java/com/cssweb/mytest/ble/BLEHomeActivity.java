package com.cssweb.mytest.ble;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cssweb.mytest.Common;
import com.cssweb.mytest.R;
import com.cssweb.mytest.threadpool.ThreadPoolManager;
import com.cssweb.mytest.utils.DateUtils;
import com.cssweb.mytest.utils.DeviceManager;
import com.cssweb.mytest.utils.HexConverter;
import com.cssweb.mytest.utils.MLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liwx on 2018/6/8.
 * 用于测试BLE 发送广播，接收广播、传输数据等
 */
public class BLEHomeActivity extends Activity {
    private static final String TAG = "BLEHomeActivity";
    private PeripheralManager mPeripheralManager;
    private Handler mMyHandler = new Handler();
    private TextView mTextView;
    private StringBuilder mStringBuilder = new StringBuilder();
    private CenterManager mCenterManager;

    private Timer mTimer = new Timer();
    private TimerTask mBLEScanTask;
    private TimerTask mBLEAdvertiseTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        int flags =
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;

        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= flags;
        getWindow().setAttributes(lp);
        setContentView(R.layout.layout_periphral);
        mPeripheralManager = new PeripheralManager(BLEHomeActivity.this);
        mPeripheralManager.setOnPeripheralCallback(mOnPeripheralCallback);
        mCenterManager = new CenterManager(BLEHomeActivity.this);
        mTextView = (TextView) findViewById(R.id.tv_log);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvertise();
            }
        });

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBLEBroadcast();
            }
        });

        initTask();
        startBLETask();//开始BLE定时任务
        //        GsonManager.generateGson();
        //        threadTest();
        //        sortTest();
        setScreenBrightness();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBLETask();
    }

    /**
     * 发送BLE广播
     */
    private void startAdvertise() {
        String hexData = "88997766";
        mPeripheralManager.stopAdvertise();
        int startResult = mPeripheralManager.preStartAdvertise(Common.UUID_SERVICE_ENTRY_GATE, Common.UUID_CHARACTERISTIC_DATA_SHARE,
            HexConverter.hexStringToBytes(hexData), true);
        displayLogData("广播结果:" + startResult);
    }

    /**
     * 周边设备回调
     */
    private PeripheralManager.OnPeripheralCallback mOnPeripheralCallback = new PeripheralManager.OnPeripheralCallback() {
        @Override
        public void onAdvertiseSuccess(final String advData) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayLogData("广播成功  " + advData);
                }
            });
        }

        @Override
        public void onAdvertiseFailed(final int code) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayLogData("广播失败 " + code);
                }
            });
        }

        @Override
        public void onGattConnectSuccess() {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayLogData("onGattConnectSuccess");
                }
            });
            //            stopHeartBeat();
            //            startHeartBeat();
        }

        @Override
        public void onGattDisConnect(final int code) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayLogData("onGattDisConnect  " + code);
                }
            });
        }

        @Override
        public void onPeripheralReceiveData(final byte[] data) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    String hexData = HexConverter.bytesToHexString(data);
                    displayLogData("onPeripheralReceiveData  =" + hexData);
                }
            });

        }

        @Override
        public void onCharacteristicReadRequest() {
            displayLogData("onCharacteristicReadRequest");
            //            Log.d(TAG, "onCharacteristicReadRequest");
            //            try {
            //                mMyHandler.postDelayed(new Runnable() {
            //                    @Override
            //                    public void run() {
            //                        List<QrCodeTicket> qrCodeTicketList = DBManager.getLocalQrCodeTicketOrderByHandleTime();
            //                        if (qrCodeTicketList != null) {
            //                            for (int i = 0; i < qrCodeTicketList.size(); i++) {
            //                                QrCodeTicket ticket = qrCodeTicketList.get(i);
            //                                if (i < 3 && ticket.getHandleDateTimes() != 0) {
            //                                    /**数据格式：
            //                                     *SjtId -hex 4字节
            //                                     *SjtStatus - hex 1字节
            //                                     *DeviceCode - bcd 5 字节
            //                                     * trxType -  hex 1字节
            //                                     * trxAmount - 2字节
            //                                     *handleDate -bcd 7字节
            //                                     * */
            //                                    Log.d(TAG, "### " + ticket.toString());
            //                                    //                        mPeripheralManager.notifyPhoneData(byteData);
            //                                    byte[] bytesData = new byte[16];
            //                                    byte[] byteSjtId = buildSjtId(ticket.getSjtId());
            //                                    byte[] byteSjtStatus = ByteConverter.str2Bcd(ticket.getSjtStatus());
            //                                    byte[] byteDeviceCode = ByteConverter.str2Bcd(ticket.getDeviceCode());
            //                                    byte[] byteTrxType = ByteConverter.str2Bcd(ticket.getTrxType());
            //                                    byte[] byteTrxAmount = HexConverter.hexStringToBytes(Utils.addLeftChar(Integer.toHexString(ticket
            // .getTrxAmount()), 4, "0"));
            //                                    byte[] byteHandleDate = ByteConverter.str2Bcd(String.valueOf(ticket.getHandleDateTimes())
            // .substring(8, 14));
            //                                    displayLogData("handle date = " + HexConverter.bytesToHexString(byteHandleDate));
            //                                    Log.d(TAG, " byteSjtId = " + HexConverter.bytesToHexString(byteSjtId));
            //                                    Log.d(TAG, " byteSjtId = " + ByteConverter.byteToInt(byteSjtId));
            //                                    Log.d(TAG, " byteSjtStatus = " + HexConverter.bytesToHexString(byteSjtStatus));
            //                                    Log.d(TAG, " byteDeviceCode = " + HexConverter.bytesToHexString(byteDeviceCode));
            //                                    Log.d(TAG, " byteTrxType = " + HexConverter.bytesToHexString(byteTrxType));
            //                                    Log.d(TAG, " byteTrxAmount = " + HexConverter.bytesToHexString(byteTrxAmount));
            //                                    Log.d(TAG, " byteHandleDate = " + HexConverter.bytesToHexString(byteHandleDate));
            //
            //                                    System.arraycopy(byteSjtId, 0, bytesData, 0, byteSjtId.length);
            //                                    System.arraycopy(byteSjtStatus, 0, bytesData, byteSjtId.length, byteSjtStatus.length);
            //                                    System.arraycopy(byteDeviceCode, 0, bytesData, byteSjtId.length + byteSjtStatus.length,
            // byteDeviceCode.length);
            //                                    System.arraycopy(byteTrxType, 0, bytesData, byteSjtId.length + byteDeviceCode.length +
            // byteSjtStatus.length, byteTrxType.length);
            //                                    System.arraycopy(byteTrxAmount, 0, bytesData, byteSjtId.length + byteDeviceCode.length +
            // byteSjtStatus.length + byteTrxType.length, byteTrxAmount.length);
            //                                    System.arraycopy(byteHandleDate, 0, bytesData, byteSjtId.length + byteDeviceCode.length +
            // byteSjtStatus.length + byteTrxType.length + byteTrxAmount.length, byteHandleDate.length);
            //
            //                                    String ticketData = HexConverter.bytesToHexString(bytesData);
            //                                    //                                    Log.d(TAG, "HEX DATA = " + ticketData);
            //                                    displayLogData("onCharacteristicReadRequest notifyData = " + ticketData);
            //                                    mPeripheralManager.notifyData(HexConverter.hexStringToBytes(ticketData));
            //
            //                                } else {
            //                                    Log.d(TAG, "### not send");
            //                                }
            //                            }
            //                        }
            //                    }
            //                }, 100);
            //            } catch (Exception e) {
            //                Log.d(TAG, "onCharacteristicReadRequest occur error:", e);
            //            }
        }
    };

    private void displayLogData(String data) {
        MLog.d(TAG, data);
        MLog.d(TAG, "StringBuilder 长度" + mStringBuilder.length());
        if (mStringBuilder.length() > 30000) {
            mStringBuilder.delete(0, mStringBuilder.length());
        }
        mStringBuilder.insert(0, DateUtils.formatDate2(System.currentTimeMillis()) + " 【" + data + "】" + "\n");
        //        mStringBuilder.append(DateUtils.formatDate2(System.currentTimeMillis()) + " 【" + data + "】").append("\n");
        mMyHandler.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(mStringBuilder.toString());
            }
        });
    }

    static DecimalFormat df = new DecimalFormat("######0");

    public static String formatDouble2f(double value) {
        return df.format(value);
    }

    /**
     * 分转元
     */
    public static String parseMoney(double amount) {
        return formatDouble2f(amount / 100);
    }

    private void gsonTest() {

    }

    private void threadTest() {
        ThreadPoolManager.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "---------");
            }
        });
    }

    private void sortTest() {
        Log.d(TAG, "---" + ("Y".equalsIgnoreCase(null)));
        List<A> list = new ArrayList<>();
        A a;
        for (int i = 0; i < 10; i++) {
            a = new A();
            a.distance = (int) (Math.random() * 10);
            list.add(a);
        }
        Comparator comp = new SortComparator();
        Collections.sort(list, comp);
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "sort = " + list.get(i).toString());
        }
    }

    public class A {
        public int distance;

        @Override
        public String toString() {
            return "A{" + "distance=" + distance + '}';
        }
    }

    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            A a = (A) lhs;
            A b = (A) rhs;

            return (a.distance - b.distance);
        }
    }

    /**
     * 扫描广播
     */
    private void scanBLEBroadcast() {
        mCenterManager.startScanLeDevice(Common.UUID_SERVICE_ENTRY_GATE, -1);
        mCenterManager.setOnCenterCallback(new CenterManager.OnCenterCallback() {
            @Override
            public void onStartScan() {
                displayLogData("开始扫描");

            }

            @Override
            public void onScanComplete(ArrayList<ScanResult> list) {
                displayLogData("扫描完成");

            }

            @Override
            public void onScanFailed(int code) {
                displayLogData("扫描失败");

            }

            @Override
            public void onScanCompleteNewApi(ScanResult scanResult) {
                MLog.d(TAG, "onScanCompleteNewApi");
                final String data = BLEDataParser.parseNewApiAdvertise(scanResult);
                if (data != null) {
                    displayLogData("解析到的广播数据:" + data.toString());
                } else {
                    displayLogData("解析广播数据异常");
                }
                //                startAdvertise();
            }

            @Override
            public void onScanCompleteOldApi(byte[] scanRecord) {

            }

            @Override
            public void onGattServiceDiscovered(BluetoothGatt gatt) {

            }

            @Override
            public void onGattServiceDiscoveredFailed(int code) {

            }

            @Override
            public void onConnectSuccess(BluetoothGatt gatt) {

            }

            @Override
            public void onConnectFailed(int code) {

            }

            @Override
            public void onWriteDataSuccess() {

            }

            @Override
            public void onWriteDataFailed(int code) {

            }

            @Override
            public void onCenterReceiveData(String data) {

            }
        });
    }

    private void startBLETask() {
        mTimer.schedule(mBLEScanTask, 0, 1500 * 10);
        mTimer.schedule(mBLEAdvertiseTask, 0, 1500 * 10);
    }


    private void stopBLETask() {
        mBLEScanTask.cancel();
        mBLEAdvertiseTask.cancel();
        mTimer.cancel();
    }

    private void initTask() {
        mBLEScanTask = new TimerTask() {
            @Override
            public void run() {
                displayLogData("扫描中......");
                mCenterManager.stopScan();
                scanBLEBroadcast();
                setScreenOn();
            }
        };
        mBLEAdvertiseTask = new TimerTask() {
            @Override
            public void run() {
                displayLogData("广播中......");
                startAdvertise();
                setScreenOn();
            }
        };
    }

    /**
     * 点亮屏幕
     */
    private void setScreenOn() {
        boolean isScreenOn = DeviceManager.isScreenOn(getApplicationContext());
        MLog.d(TAG, "is screen on = " + isScreenOn);
        if (!isScreenOn) {
            DeviceManager.clickPowerBtn();
        }
    }


    private void setScreenBrightness() {
        int paramInt = 255;
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

}

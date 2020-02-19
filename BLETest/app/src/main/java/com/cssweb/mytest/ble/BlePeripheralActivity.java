package com.cssweb.mytest.ble;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cssweb.mytest.Common;
import com.cssweb.mytest.utils.DateUtils;
import com.cssweb.mytest.utils.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.gson.GsonManager;
import com.cssweb.mytest.threadpool.ThreadPoolManager;
import com.cssweb.mytest.utils.MLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liwx on 2018/6/8.
 */
public class BlePeripheralActivity extends Activity {
    private static final String TAG = "BlePeripheralActivity";
    private PeripheralManager mPeripheralManager;
    private Handler mMyHandler = new Handler();
    private TextView mTextView;
    private StringBuilder mStringBuilder = new StringBuilder();
    private CenterManager mCenterManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.layout_periphral);
        mPeripheralManager = new PeripheralManager(BlePeripheralActivity.this);
        mPeripheralManager.setOnPeripheralCallback(mOnPeripheralCallback);
        mCenterManager = new CenterManager(BlePeripheralActivity.this);
        mTextView = (TextView) findViewById(R.id.tv_log);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvertise();
            }
        });

        Log.d(TAG, "-------" + TextUtils.equals("11", null));
        Log.d(TAG, "------222-" + parseMoney(300));
        GsonManager.generateGson();
        //        threadTest();
        //        sortTest();

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBLEBroadcast();
            }
        });

    }

    /**
     * 发送BLE广播
     */
    private void startAdvertise() {
        String hexData = "88997766";
        mPeripheralManager.startAdvertise(Common.UUID_SERVICE_ENTRY_GATE, Common.UUID_CHARACTERISTIC_DATA_SHARE,
            HexConverter.hexStringToBytes(hexData));
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
                    displayLogData("onAdvertiseSuccess  " + advData);
                }
            });
        }

        @Override
        public void onAdvertiseFailed(final int code) {
            mMyHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayLogData("onAdvertiseFailed " + code);
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
        mStringBuilder.append(DateUtils.formatDate2(System.currentTimeMillis())+" 【"+data+"】").append("\n");

        mTextView.setText(mStringBuilder.toString());
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

    private void scanBLEBroadcast() {
        mCenterManager.startScanLeDevice(Common.UUID_SERVICE_ENTRY_GATE, 10000);
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
                final BroadCastData data = BLEDataParser.parseNewApi(scanResult);
                if (data != null) {
                    displayLogData("解析到的广播数据:" + data.toString());
                } else {
                    displayLogData("解析广播数据异常");
                }
                startAdvertise();
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

}

package com.cssweb.mytest.ble.scandevice;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cssweb.mytest.CustomDialog;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.cssweb.mytest.Utils;
import com.cssweb.mytest.utils.ByteConverter;

import org.apache.commons.net.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.net.ssl.SSLException;

/**
 * Created by lenovo on 2017/5/31.
 * <p>
 * 用于测试扫描PAD 广播数据
 */

public class ScanDeviceActivity extends FragmentActivity {
    private static final String TAG = "ScanDeviceActivity";
    private Button mBtnRefresh;

    private ListView mListView;

    private List<ScanResult> mScanResultList = new ArrayList<>();
    private static final int SCAN_DEVICE_TIME = 1000 * 3;

    public static final UUID UUID_CHARACTERISTIC_DATA_SHARE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");//用于广播设备编号
    CenterManager mCenterManager;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler();
    private MyAdapter mMyAdapter;
    private ArrayList<UUID> mUUIDArrayList = new ArrayList<>();

    int mScanType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);
        getDeviceInfo();

        mBtnRefresh = (Button) findViewById(R.id.btn_refresh);
        mListView = (ListView) findViewById(R.id.lv_device);
        mCenterManager = new CenterManager(ScanDeviceActivity.this);
        mCenterManager.setOnCenterCallback(mCenterCallback);
        mProgressDialog = new ProgressDialog(ScanDeviceActivity.this);
        mMyAdapter = new MyAdapter();
        mListView.setAdapter(mMyAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mUUIDArrayList.add(Common.UUID_SERVICE_ENTRY_GATE);
        mUUIDArrayList.add(Common.UUID_SERVICE_EXIT_GATE);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScanType = 1;
                mScanResultList.clear();
                mMyAdapter.notifyDataSetChanged();

                mProgressDialog.show();

                mCenterManager.startScanLeDevice(mUUIDArrayList, SCAN_DEVICE_TIME);
                //                Intent intent = new Intent();
                //                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                //                intent.setAction(Intent.ACTION_MAIN);
                //                intent.setComponent(new ComponentName("com.cssweb.shankephone", "com.cssweb.shankephone.login.SplashActivity"));
                //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //                startActivity(intent);
            }
        });


        findViewById(R.id.btn_refresh2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScanType = 2;
                mScanResultList.clear();
                mMyAdapter.notifyDataSetChanged();

                mProgressDialog.show();

                mCenterManager.startScanLeDevice(mUUIDArrayList, SCAN_DEVICE_TIME);
            }
        });

        //        try {
        //            new Thread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    try {
        //                        hahaTest();
        //                    } catch (Exception e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //            }).start();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        List<String> list = new ArrayList<>();
        list.add("C口");
        list.add("A口");
        list.add("D口");
        list.add("出入口");

        Collections.sort(list, new FileComparator());
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "after sort = " + list.get(i));
        }

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "### YEAR = " + year + " month =" + month + " day = " + day);

        String key = "BD565ABD735DCDC1497B29B71D35122C";
        String org = "5259603120170726";
        Log.d(TAG, "HEX key = " + HexConverter.bytesToHexString(key.getBytes()));
        try {
            String security = encryptThreeDESECB(org, key);
            System.out.println("-----" + security);
            System.out.println("-----" + decryptThreeDESECB(security, key));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String encryptThreeDESECB(String src, String key) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte[] b = cipher.doFinal(src.getBytes());

        Base64 encoder = new Base64();
        return encoder.encodeToString(b).replaceAll("\r", "").replaceAll("\n", "");

    }

    //3DESECB解密,key必须是长度大于等于 3*8 = 24 位
    public static String decryptThreeDESECB(String src, String key) throws Exception {
        //--通过base64,将字符串转成byte数组
        Base64 decoder = new Base64();
        byte[] bytesrc = decoder.decode(src);
        //--解密的key
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        //--Chipher对象解密
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        byte[] retByte = cipher.doFinal(bytesrc);

        return new String(retByte);
    }


    private void getDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                builder.append(field.getName() + "---" + field.get(null).toString() + "\n");
                //                mInfosMap.put(field.getName(), field.get(null).toString());
                // MLog.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
            }
        }

        Log.d(TAG, "----" + builder.toString());
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mScanResultList.size() > 0) {
                showProgress("正在连接");
                mCenterManager.connect(mScanResultList.get(position).getDevice().getAddress());
            }
        }
    };

    CenterManager.OnCenterCallback mCenterCallback = new CenterManager.OnCenterCallback() {
        @Override
        public void onStartScan() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                }
            });
        }

        @Override
        public void onScanComplete(final ArrayList<ScanResult> list) {
            mHandler.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    mScanResultList.clear();
                    for (ScanResult result : list) {
                        BluetoothDevice device = result.getDevice();
                        if (device != null) {
                            if (mScanResultList.size() == 0) {
                                mScanResultList.add(result);
                            } else {
                                boolean isAdd = false;
                                for (ScanResult scanResult : mScanResultList) {
                                    BluetoothDevice deviceAdded = scanResult.getDevice();
                                    if (device.getAddress().equals(deviceAdded.getAddress())) {
                                        isAdd = true;
                                    }
                                }
                                if (!isAdd) {
                                    mScanResultList.add(result);
                                }
                            }
                        }
                    }
                    mProgressDialog.dismiss();
                    mMyAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onScanFailed(int code) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "扫描失败,重启客户端", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onGattServiceDiscovered(final BluetoothGatt gatt) {
            dissMissProgress();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    List<BluetoothGattService> list = gatt.getServices();
                    if (list != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (BluetoothGattService service : list) {
                            UUID uuid = service.getUuid();
                            if (uuid.toString().equalsIgnoreCase("000001FA-0000-1000-8000-00805f9b34fb") || uuid.toString().equalsIgnoreCase("000001FB-0000-1000-8000-00805f9b34fb")) {
                                Log.d(TAG, "设置可通知");
                                BluetoothGattCharacteristic characteristic = service.getCharacteristic(Common.UUID_CHARACTERISTIC_DATA_SHARE);
                                gatt.setCharacteristicNotification(characteristic, true);
                                characteristic.setValue(HexConverter.hexStringToBytes("9520"));
                                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Common.UUID_CHARACTERISTIC_DATA_SHARE);
                                if (descriptor != null) {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    descriptor.setValue(HexConverter.hexStringToBytes("9520"));
                                    //                                    final boolean result = gatt.writeDescriptor(descriptor);
                                    //                                    Log.d(TAG, "writeDescriptor result = " + result);
                                } else
                                    Log.d(TAG, "descriptor is null");
                                gatt.readCharacteristic(characteristic);
                            }

                            stringBuilder.append("UUID = " + uuid.toString()).append("\n");
                        }
                        CustomDialog.showDialog(ScanDeviceActivity.this, stringBuilder.toString());
                    }

                }
            });

        }

        @Override
        public void onGattServiceDiscoveredFailed(int code) {
            dissMissProgress();
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectSuccess(BluetoothGatt gatt) {
            showProgress("查找服务");
            boolean startDiscoverResult = gatt.discoverServices();
        }

        @Override
        public void onConnectFailed(int code) {
            dissMissProgress();

        }

        @Override
        public void onWriteDataSuccess() {
            dissMissProgress();

        }

        @Override
        public void onWriteDataFailed(int code) {
            dissMissProgress();

        }

        @Override
        public void onCenterReceiveData(String data) {
            Log.d(TAG, "onCenterReceiveData  = " + data);
            showProgress(data);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mCenterManager.disconnect();
    }

    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mScanResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return mScanResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = ((LayoutInflater) ScanDeviceActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_scan_device_list, null, false);
                holder.mTvAddress = (TextView) convertView.findViewById(R.id.tv_device_address);
                holder.mTvAdvData = (TextView) convertView.findViewById(R.id.tv_adv_data);
                holder.mTvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ScanResult scanResult = mScanResultList.get(position);
            BluetoothDevice device = scanResult.getDevice();
            ScanRecord scanRecord = scanResult.getScanRecord();
            if (scanRecord != null) {
                String data = getAdvData(scanRecord);
                if (!TextUtils.isEmpty(data)) {
                    StringBuilder builder = new StringBuilder();
                    byte[] bytesData = HexConverter.hexStringToBytes(data);

                    if (mScanType == 1) {
                        byte[] byteSjtStatus = Arrays.copyOfRange(bytesData, 4, 5);
                        byte[] byteDeviceCode = Arrays.copyOfRange(bytesData, 5, 10);
                        byte[] byteTrxType = Arrays.copyOfRange(bytesData, 10, 11);
                        byte[] byteTrxAmount = Arrays.copyOfRange(bytesData, 11, 13);
                        byte[] byteHandleDate = Arrays.copyOfRange(bytesData, 13, 16);

                        String sjtStatus = ByteConverter.bcd2Str(byteSjtStatus);
                        String deviceCode = ByteConverter.bcd2Str(byteDeviceCode);
                        String trxType = ByteConverter.bcd2Str(byteTrxType);
                        String trxAmountStr = HexConverter.bytesToHexString(byteTrxAmount);
                        String trxAmountStr2 = Utils.addLeftChar(trxAmountStr, 8, "0");
                        int trxAmount = ByteConverter.byteToInt(HexConverter.hexStringToBytes(trxAmountStr2));
                        String handleDate = format2.format(System.currentTimeMillis()) + ByteConverter.bcd2Str(byteHandleDate);

                        builder.append("广播数据").append("  " + mScanType).append("\n");
                        builder.append("sjtId：").append(parseSjtId(scanRecord)).append("\n");
                        builder.append("SjtStatus：").append(sjtStatus).append("\n");
                        builder.append("DeviceCode：").append(deviceCode).append("\n");
                        builder.append("trxType：").append(trxType).append("\n");
                        builder.append("trxAmount：").append(trxAmount).append("\n");
                        builder.append("handleDate：").append(handleDate).append("\n");
                    } else {
                        byte[] type = Arrays.copyOfRange(bytesData, 0, 1);
                        String typeStr = HexConverter.bytesToHexString(type);
                        byte[] sjtIdByte = Arrays.copyOfRange(bytesData, 1, 5);
                        int sjt1Id = ByteConverter.byte2Int(sjtIdByte);
                        String sjt1StationCode = HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 5, 9));
                        //                        String sjt1handleDate = HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 6, 9));

                        int sjt2Id = ByteConverter.byte2Int(Arrays.copyOfRange(bytesData, 9, 13));
                        String sjt2StationCode = HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 13, 17));
                        //                        String sjt2handleDate = HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 14, 17));


                        builder.append("广播数据").append("  " + mScanType).append("\n");
                        builder.append("sjtId1 ：").append(sjt1Id).append("\n");
                        builder.append("type ：").append(typeStr.substring(0, 1)).append("\n");
                        builder.append("stationCode：").append(sjt1StationCode).append("\n");
                        builder.append("handleDate：").append(buildStationCode(sjt1StationCode)).append("\n");
                        builder.append("------------").append("\n");
                        builder.append("sjtId2 ：").append(sjt2Id).append("\n");
                        builder.append("type ：").append(typeStr.substring(1, 2)).append("\n");
                        builder.append("stationCode：").append(sjt2StationCode).append("\n");
                        builder.append("handleDate：").append(buildStationCode(sjt2StationCode)).append("\n");
                        builder.append("------------").append("\n");
                    }
                    holder.mTvAdvData.setText(builder.toString());
                } else
                    holder.mTvAdvData.setText("--");
            }
            if (device != null) {
                holder.mTvDeviceName.setText("设备名称:  " + device.getName());
                holder.mTvAddress.setText("设备地址:  " + device.getAddress());
            }


            return convertView;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private String getAdvData(ScanRecord scanRecord) {
            if (scanRecord != null) {
                if (scanRecord.getServiceData() != null) {
                    byte[] deviceCode = scanRecord.getServiceData().get(new ParcelUuid(UUID_CHARACTERISTIC_DATA_SHARE));
                    if (deviceCode != null) {
                        String deviceCodeHex = HexConverter.bytesToHexString(deviceCode);
                        //                        findDeviceCode = Integer.valueOf(deviceCodeHex);
                        Log.d(TAG, "data = " + deviceCodeHex);
                        return deviceCodeHex;
                    }
                }
            }
            return "--";
        }

        class ViewHolder {
            private TextView mTvDeviceName;
            private TextView mTvAdvData;
            private TextView mTvAddress;
        }

    }

    private void showProgress(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage(msg);
                mProgressDialog.show();
            }
        });
    }

    private void dissMissProgress() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        });
    }

    public void hahaTest() throws Exception {
        File path = Environment.getExternalStorageDirectory();

        Log.d(TAG, "文件存在不111  = " + path.getPath());

        File file = new File(path.getPath() + "/c033.png");
        Log.d(TAG, "文件存在不  = " + file.exists());
        System.out.println("文件存在不  = " + file.exists());
        byte[] buff = getBytesFromFile(file);
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "fM2Rtt3ib48tsUdZ6tF_xHPVdK5l1ypG");
        map.put("api_secret", "rkBIwdAu2ImXpl_w2c016Y9Q6pB-ouTP");
        byteMap.put("image_file", buff);
        try {
            byte[] bacd = post(url, map, byteMap);
            System.out.println(new String(bacd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();

    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if (fileMap != null && fileMap.size() > 0) {
            Iterator fileIter = fileMap.entrySet().iterator();
            while (fileIter.hasNext()) {
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey() + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try {
            if (code == 200) {
                ins = conne.getInputStream();
            } else {
                ins = conne.getErrorStream();
            }
        } catch (SSLException e) {
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while ((len = ins.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }

    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    public class FileComparator implements Comparator<String> {
        public int compare(String file1, String file2) {
            return file1.compareTo(file2);
        }
    }

    /**
     * 获取设备编码
     */
    private int parseSjtId(ScanRecord scanRecord) {
        Log.d(TAG, "parseSjtId-----");
        int sjtId = -1;
        if (scanRecord != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (scanRecord.getServiceData() != null) {
                    //共9个字节，前5个字节-设备编号 bcd
                    //后4个字节-sjtId 16进制
                    byte[] broadcastData = scanRecord.getServiceData().get(new ParcelUuid(UUID_CHARACTERISTIC_DATA_SHARE));
                    if (broadcastData != null && broadcastData.length == 16) {
                        sjtId = ByteConverter.byte2Int(Arrays.copyOfRange(broadcastData, 0, 4));
                        Log.d(TAG, "broadcast sjtId = " + sjtId);
                    } else {
                        Log.d(TAG, "broadcast data is null or length is invalid " + (broadcastData != null) + " length = " + (broadcastData != null ? broadcastData.length : 0));
                    }
                } else {
                    Log.d(TAG, "ServiceData is null ");
                }
            } else {
                Log.d(TAG, "current os version is = " + Build.VERSION.SDK_INT);
            }
        } else {
            Log.d(TAG, "scanRecord is null ");
        }
        return sjtId;
    }

    /**
     * 3字节byte转换成int
     */
    private int sjtId3ByteToInt(byte[] byteSjtId) {
        byte[] sjtId = new byte[4];
        System.arraycopy(byteSjtId, 0, sjtId, 1, byteSjtId.length);
        return ByteConverter.byteToInt(sjtId);
    }

    private String buildStationCode(String orgDeviceCode) {
        String deviceCode = "";
        if (!TextUtils.isEmpty(orgDeviceCode)) {
            deviceCode = orgDeviceCode.substring(0, 4) + "04" + orgDeviceCode.substring(4, 8);
        }
        return deviceCode;
    }

}

package com.cssweb.mytest.qrcodejni;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.cssweb.framework.utils.MLog;
import com.cssweb.jni.CNACrypto;
import com.cssweb.jni.QRCodeUsbPosImpl;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lenovo on 2017/7/6.
 * 调用SO文件 读取二维码数据
 */

public class ReadQrCodeActivity extends FragmentActivity {
    private static final String TAG = "ReadQrCodeActivity";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:", Locale.getDefault());

    static {
        Log.d("123123", "QRCodeUsbPosImpl-----------1");
        System.loadLibrary("qrcode");
        //        System.loadLibrary("usb1.0Android");
        System.load("/system/lib/libssl.so");
        System.load("/system/lib/libcrypto.so");
        Log.d("123123", "QRCodeUsbPosImpl-----------2");
        System.loadLibrary("cnacryptoso");
        Log.d("123123", "QRCodeUsbPosImpl-----------3");
    }

    QRCodeUsbPosImpl qrCodeUsbPos = new QRCodeUsbPosImpl();
    CNACrypto mCNACrypto = new CNACrypto();
    private Timer mTimer = new Timer();//获取二维码token定时器
    private TimerTask mQrCodeTask;//刷新二维码内容task
    private TextView mTextView;
    Handler mHandler = new Handler();
    StringBuilder mStringBuilder = new StringBuilder();
    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jnitest);
        mTextView = (TextView) findViewById(R.id.tv_receive_data);
        lookDeviceInfo();
        Log.d(TAG, "onCreate");
        //        qrCodeTest();
        encryptTest();
    }


    private String org = "ABBA12344321CD";
    private byte[] mSignRes = new byte[64];
    String keyX = "CA0E5E2B3A7D379ED4C7F03F0C329E6A31D866169FBEB86A246E2BA022F8B938";
    String keyY = "6B93791FC6F2D6264095EA10F3E7F94003AC9983F6BD0C9A7615D035C47EDE76";
    String privateKey = "205F05024063476B7678A218B06CE124D06402AD370183C6198FB03F3B382009";

    private void encryptTest() {
        try {
            int initResult = mCNACrypto.CNA_sm2_init_std_curve();
            int result = mCNACrypto.CNA_sm2_set_public_key(getBytesUTF8(keyX), getBytesUTF8(keyY));
            Log.d(TAG, "#### " + "initResult = " + initResult + "  set_public_key result = " + result);
            int set_uid_res = mCNACrypto.CNA_sm2_set_uid(getBytesUTF8("4401"));
            mCNACrypto.CNA_sm2_set_private_key(getBytesUTF8(privateKey));
            Log.d(TAG, "#### set_uid RES = " + set_uid_res);
            int encryptRes = mCNACrypto.CNA_sm2_sign(getBytesUTF8(org), mSignRes);
            Log.d(TAG, "#### CNA_sm2_sign RES = " + encryptRes);

            Log.d(TAG, "#### 开始验签 ");
            int verify_with_sm3 = mCNACrypto.CNA_sm2_verify_with_sm3(mSignRes, getBytesUTF8(org));

            Log.d(TAG, "#### CNA_sm2_verify_with_sm3 = " + verify_with_sm3);

            int cna_free_res = mCNACrypto.CNA_free();
            Log.d(TAG, "#### CNA_free RES = " + cna_free_res);
            if (mSignRes != null) {
                Log.d(TAG, "#### 签名后 = " + HexConverter.bytesToHexString(mSignRes));
            } else {
                Log.d(TAG, "#### 签名数据 空！！  ");
            }


        } catch (Exception e) {
            Log.d(TAG, "#### 异常啦--------");
            e.printStackTrace();
        }

    }

    private byte[] getBytesUTF8(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void qrCodeTest() {
        Log.d(TAG, "  ----- " + qrCodeUsbPos.Init());
        Log.d(TAG, "  ----GetStatus- " + qrCodeUsbPos.GetStatus());
        qrCodeUsbPos.StartWork();
        mQrCodeTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "  ----start ");
                final long millisSecond = System.currentTimeMillis();
                Future<String> signFuture = mExecutorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        byte[] data = qrCodeUsbPos.GetQRCodeString();
                        Log.d(TAG, "  GetQRCodeString-");
                        Log.d(TAG, "  GetQRCodeString : " + HexConverter.bytesToHexString(data));
                        if (data != null) {
                            mStringBuilder.append("byte 长度 = " + data.length).append("\n");
                        }
                        mStringBuilder.append(dateFormat.format(System.currentTimeMillis()) + "---" + HexConverter.bytesToHexString(data)).append("\n");
                        return "";
                    }
                });
                try {
                    String res = signFuture.get();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "  ----Exception- ");
                }
                long gap = System.currentTimeMillis() - millisSecond;
                MLog.d(TAG, "sign Time : " + gap);
                Log.d(TAG, "  ----stop ");
            }
        };
        qrCodeUsbPos.StopWork();
        mTimer.schedule(mQrCodeTask, 0, 3000);
    }

    private void lookDeviceInfo() {
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
        Log.i(TAG, "----" + builder.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

}

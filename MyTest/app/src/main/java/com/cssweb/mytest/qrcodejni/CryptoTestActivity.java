package com.cssweb.mytest.qrcodejni;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cssweb.framework.utils.MLog;
import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;

import java.lang.reflect.Field;

/**
 * Created by liwx on 2017/8/17.
 */

public class CryptoTestActivity extends FragmentActivity {
    private static final String TAG = "CryptoTestActivity";
    private CryptoManager mCryptoManager;

    private String org = "ABBA12344321CD";
    private byte[] mSignRes = new byte[64];
    String keyX = "CA0E5E2B3A7D379ED4C7F03F0C329E6A31D866169FBEB86A246E2BA022F8B938";
    String keyY = "6B93791FC6F2D6264095EA10F3E7F94003AC9983F6BD0C9A7615D035C47EDE76";
    String privateKey = "205F05024063476B7678A218B06CE124D06402AD370183C6198FB03F3B382009";
    String uId = "4401";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jnitest);
        Log.d(TAG, "onCreate");
        lookDeviceInfo();
        mCryptoManager = new CryptoManager(this);
        mCryptoManager.signData(keyX, keyY, privateKey, uId, org, new CryptoManager.CryptoListener() {
            @Override
            public void onSignSuccess(final byte[] singData) {
                MLog.d(TAG, "onSignSuccess " + HexConverter.bytesToHexString(singData));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verifyData(singData);
                    }
                }, 1000);

            }

            @Override
            public void onVerifySuccess() {

            }

            @Override
            public void onOptionFailed(int errCode, int nativeCode) {
                MLog.d(TAG, "sign failed " + errCode + " " + nativeCode);
            }
        });

    }

    private void verifyData(byte[] singData) {
        mCryptoManager.verifyData(keyX, keyY, privateKey, uId, singData, org, new CryptoManager.CryptoListener() {
            @Override
            public void onSignSuccess(byte[] singData) {

            }

            @Override
            public void onVerifySuccess() {
                MLog.d(TAG, "onVerifySuccess");
            }

            @Override
            public void onOptionFailed(int errCode, int nativeCode) {
                MLog.d(TAG, "verify failed " + errCode + " " + nativeCode);
            }
        });
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
}

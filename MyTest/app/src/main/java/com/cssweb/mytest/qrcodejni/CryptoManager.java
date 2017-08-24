package com.cssweb.mytest.qrcodejni;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cssweb.framework.utils.HexConverter;
import com.cssweb.framework.utils.MLog;
import com.cssweb.jni.CNACrypto;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

/**
 * Created by liwx on 2017/8/17.
 */

public class CryptoManager {
    private static final String TAG = "CryptoManager";

    public static final int CODE_INIT_FAILED = 100;//初始化失败
    public static final int CODE_SET_PUBLIC_KEY_FAILED = 101;//设置公钥失败
    public static final int CODE_SET_PRIVATE_KEY_FAILED = 102;//设置私钥失败
    public static final int CODE_SET_UID_FAILED = 103;//设置UID失败
    public static final int CODE_SING_FAILED = 104;//签名失败
    public static final int CODE_VERIFY_FAILED = 105;//验签失败
    public static final int CODE_DEFAL_FAILED = 999;//未知异常


    private CryptoHandler mHandler;
    private String mKeyX;
    private String mKeyY;
    private String mPrivateKey;
    private String mOrgData;

    static {
        Log.d("123123", "QRCodeUsbPosImpl-----------1");
        System.loadLibrary("qrcode");
        System.loadLibrary("usb1.0Android");
        System.load("/system/lib/libcrypto.so");
        System.load("/system/lib/libssl.so");
        Log.d("123123", "QRCodeUsbPosImpl-----------2");
        System.loadLibrary("cnacryptoso");
        Log.d("123123", "QRCodeUsbPosImpl-----------3");
    }

    private CNACrypto mCNACrypto;

    CryptoManager(Activity activity) {
        mHandler = new CryptoHandler(activity);
        mCNACrypto = new CNACrypto();
    }

    private static class CryptoHandler extends Handler {
        public static final int STEP_INIT_SUCCESS = 100;
        public static final int STEP_SET_PUB_KEY_SUCCESS = 101;
        public static final int STEP_SET_PRIVATE_SUCCESS = 102;
        public static final int STEP_SET_UID_SUCCESS = 103;
        public static final int STEP_SIGN_SUCCESS = 104;
        public static final int STEP_VERIFY_SUCCESS = 105;

        WeakReference<Activity> mWeakReference;

        CryptoHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STEP_INIT_SUCCESS:
                    break;
                case STEP_SET_PUB_KEY_SUCCESS:
                    break;
                case STEP_SET_PRIVATE_SUCCESS:
                    break;
                case STEP_SET_UID_SUCCESS:
                    break;
                case STEP_SIGN_SUCCESS:
                    break;
                case STEP_VERIFY_SUCCESS:
                    break;
            }
        }
    }

    public void signData(@NonNull String keyX, @NonNull String keyY, @NonNull String privateKey, @NonNull String uId, @NonNull final String orgData, @NonNull final CryptoListener listener) {
        try {
            MLog.d(TAG, "start sign.....");
            final long millisSecond = System.currentTimeMillis();
            initCrypto(keyX, keyY, privateKey, uId, new InitListener() {
                @Override
                public void onInitSuccess() {
                    byte[] signData = new byte[64];
                    int signRes = mCNACrypto.CNA_sm2_sign(getBytesUTF8(orgData), signData);
                    MLog.d(TAG, "CNA_sm2_sign res = " + signRes);
                    if (signRes == 0) {
                        MLog.d(TAG, "sign data = " + HexConverter.bytesToHexString(signData));
                        long gap = System.currentTimeMillis() - millisSecond;
                        MLog.d(TAG, "sign Time : " + gap);
                        listener.onSignSuccess(signData);
                        freeMemory();
                    } else {
                        handleOptFailed(listener, CODE_SING_FAILED, signRes);
                    }
                }

                @Override
                public void onInitFailed(int errCode, int nativeCode) {
                    handleOptFailed(listener, errCode, nativeCode);
                }
            });
        } catch (Exception e) {
            MLog.d(TAG, " sign data occur error:", e);
            handleOptFailed(listener, CODE_DEFAL_FAILED, -1);
            freeMemory();
        }
    }

    private void freeMemory() {
        try {
            int res = mCNACrypto.CNA_free();
            MLog.d(TAG, "CNA_free res = " + res);
        } catch (Exception e) {
            MLog.d(TAG, " freeMemory occur error: ", e);
        }
    }

    public void verifyData(@NonNull String keyX, @NonNull String keyY, @NonNull String privateKey, @NonNull String uId, @NonNull final byte[] signData, @NonNull final String orgData, @NonNull final CryptoListener listener) {
        try {
            MLog.d(TAG, "start verify.....");
            final long millisSecond = System.currentTimeMillis();
            initCrypto(keyX, keyY, privateKey, uId, new InitListener() {
                @Override
                public void onInitSuccess() {
                    int verify_with_sm3 = mCNACrypto.CNA_sm2_verify_with_sm3(signData, getBytesUTF8(orgData));
                    MLog.d(TAG, "CNA_sm2_verify_with_sm3 res = " + verify_with_sm3);
                    if (verify_with_sm3 == 0) {
                        long gap = System.currentTimeMillis() - millisSecond;
                        MLog.d(TAG, "verify Time : " + gap);
                        listener.onVerifySuccess();
                        freeMemory();
                    } else {
                        handleOptFailed(listener, CODE_VERIFY_FAILED, verify_with_sm3);
                    }
                }

                @Override
                public void onInitFailed(int errCode, int nativeCode) {
                    handleOptFailed(listener, errCode, nativeCode);
                }
            });
        } catch (Exception e) {
            MLog.d(TAG, " sign data occur error:", e);
            handleOptFailed(listener, CODE_DEFAL_FAILED, -1);
        }
    }

    private void initCrypto(@NonNull String keyX, @NonNull String keyY, @NonNull String privateKey, @NonNull String uId, @NonNull InitListener listener) {
        try {
            int initResult = mCNACrypto.CNA_sm2_init_std_curve();
            MLog.d(TAG, "CNA_sm2_init_std_curve res = " + initResult);
            if (initResult == 0) {
                int pubKeyRes = mCNACrypto.CNA_sm2_set_public_key(getBytesUTF8(keyX), getBytesUTF8(keyY));
                MLog.d(TAG, "CNA_sm2_set_public_key res = " + pubKeyRes);
                if (pubKeyRes == 0) {
                    int priKeyRes = mCNACrypto.CNA_sm2_set_private_key(getBytesUTF8(privateKey));
                    MLog.d(TAG, "CNA_sm2_set_private_key res = " + priKeyRes);
                    if (priKeyRes == 0) {
                        int setUidRes = mCNACrypto.CNA_sm2_set_uid(getBytesUTF8(uId));
                        MLog.d(TAG, "CNA_sm2_set_uid res = " + setUidRes);
                        if (setUidRes == 0) {
                            listener.onInitSuccess();
                        } else {
                            handleInitFailed(listener, CODE_SET_UID_FAILED, setUidRes);
                        }
                    } else {
                        handleInitFailed(listener, CODE_SET_PRIVATE_KEY_FAILED, priKeyRes);
                    }
                } else {
                    handleInitFailed(listener, CODE_SET_PUBLIC_KEY_FAILED, pubKeyRes);
                }
            } else {
                handleInitFailed(listener, CODE_INIT_FAILED, initResult);
            }
        } catch (Exception e) {
            MLog.d(TAG, " sign data occur error:", e);
            handleInitFailed(listener, CODE_DEFAL_FAILED, -1);
        }
    }

    private void handleOptFailed(@NonNull CryptoListener listener, int code, int nativeCode) {
        listener.onOptionFailed(code, nativeCode);
        freeMemory();
    }

    private void handleInitFailed(@NonNull InitListener listener, int code, int nativeCode) {
        listener.onInitFailed(code, nativeCode);
        freeMemory();
    }

    private byte[] getBytesUTF8(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    interface InitListener {
        void onInitSuccess();

        void onInitFailed(int errCode, int nativeCode);

    }

    interface CryptoListener {
        void onSignSuccess(byte[] singData);

        void onVerifySuccess();

        void onOptionFailed(int errCode, int nativeCode);
    }


}

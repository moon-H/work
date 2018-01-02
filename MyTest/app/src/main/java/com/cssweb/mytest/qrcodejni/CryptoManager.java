package com.cssweb.mytest.qrcodejni;

import android.support.annotation.NonNull;

import com.cssweb.framework.utils.HexConverter;
import com.cssweb.framework.utils.MLog;
import com.cssweb.framework.utils.Utils;
import com.cssweb.jni.CNACrypto;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static final int CODE_DEFAULT_FAILED = 999;//未知异常

 /*   typedef enum {
        EM_CNA_CODE_OK = 0 ,
            EM_CNA_CODE_P_ERROR = 1 ,
            EM_CNA_CODE_A_ERROR = 2 ,
            EM_CNA_CODE_B_ERROR = 3 ,
            EM_CNA_CODE_GX_ERROR = 4,
            EM_CNA_CODE_GY_ERROR = 5,
            EM_CNA_CODE_N_ERROR = 6,
            EM_CNA_CODE_PKX_ERROR = 7,
            EM_CNA_CODE_PKY_ERROR = 8,
            EM_CNA_CODE_D_ERROR = 9 ,

            EM_CNA_CODE_PK_INVALID = 11,
            EM_CNA_CODE_D_INVALID = 12,
            EM_CNA_CODE_P_NOT_PRIME_NUMBER = 13,
            EM_CNA_CODE_INVALID_CURVE = 14,
            EM_CNA_CODE_GXY_INVALID = 15,
            EM_CNA_CODE_BN_MEM_ERROR = 100 ,
    } EM_CNA_CODE  ;*/


    private static CryptoManager sCryptoManager;

    static {
        //        Log.d("123123", "QRCodeUsbPosImpl-----------1");
        //        System.loadLibrary("qrcode");
        //        System.loadLibrary("usb1.0Android");
        System.load("/system/lib/libcrypto.so");
        System.load("/system/lib/libssl.so");
        //        Log.d("123123", "QRCodeUsbPosImpl-----------2");
        System.loadLibrary("cnacryptoso");
        //        Log.d("123123", "QRCodeUsbPosImpl-----------3");
    }

    private CNACrypto mCNACrypto;
    private ExecutorService mExecutorService;
    private byte[] mSignData;
    private int mYBit = -1;
    private boolean mVerifyResult = false;

    public static CryptoManager getInstance() {
        if (sCryptoManager == null) {
            sCryptoManager = new CryptoManager();
        }
        return sCryptoManager;
    }


    private CryptoManager() {
        mCNACrypto = new CNACrypto();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    private void freeMemory() {
        try {
            int res = mCNACrypto.CNA_free();
            MLog.d(TAG, "CNA_free res = " + res);
        } catch (Exception e) {
            MLog.d(TAG, " freeMemory occur error: ", e);
        }
    }

    public byte[] signData(@NonNull final String userPubKey, @NonNull final String privateKey, @NonNull final String uId, @NonNull final String orgData) {
        MLog.d(TAG, "start sign.....");
        Future<byte[]> signFuture = mExecutorService.submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                try {
                    MLog.d(TAG, " main thread = " + Utils.isInMainThread());
                    final long millisSecond = System.currentTimeMillis();
                    initCrypto(userPubKey, privateKey, uId, new InitListener() {
                        @Override
                        public void onInitSuccess() {
                            byte[] signData = new byte[64];
                            int signRes = mCNACrypto.CNA_sm2_sign(getBytesUTF8(orgData), signData);
                            MLog.d(TAG, "CNA_sm2_sign res = " + signRes);
                            if (signRes == 0) {
                                MLog.d(TAG, "sign data = " + HexConverter.bytesToHexString(signData));
                                long gap = System.currentTimeMillis() - millisSecond;
                                MLog.d(TAG, "sign Time : " + gap);
                                mSignData = signData;
                                freeMemory();
                            } else {
                                handleOptFailed(CODE_SING_FAILED, signRes);
                                mSignData = null;
                            }
                        }

                        @Override
                        public void onInitFailed(int errCode, int nativeCode) {
                            handleOptFailed(errCode, nativeCode);
                            mSignData = null;
                        }
                    });
                } catch (Exception e) {
                    MLog.d(TAG, " sign data occur error:", e);
                    handleOptFailed(CODE_DEFAULT_FAILED, -1);
                    mSignData = null;
                }
                return mSignData;
            }
        });
        try {
            return signFuture.get();
        } catch (Exception e) {
            MLog.d(TAG, " sign data occur error:", e);
        }
        return null;
    }

    /**
     * 验签
     */
    public boolean verifyData(@NonNull final String userPubKey, @NonNull final String privateKey, @NonNull final String uId, @NonNull final byte[] signData, @NonNull final String orgData) {
        mVerifyResult = false;
        MLog.d(TAG, "start verify.....");
        Future<Boolean> verifyFuture = mExecutorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    final long millisSecond = System.currentTimeMillis();
                    initCrypto(userPubKey, privateKey, uId, new InitListener() {
                        @Override
                        public void onInitSuccess() {
                            int verify_with_sm3 = mCNACrypto.CNA_sm2_verify_with_sm3(signData, getBytesUTF8(orgData));
                            MLog.d(TAG, "CNA_sm2_verify_with_sm3 res = " + verify_with_sm3);
                            if (verify_with_sm3 == 0) {
                                long gap = System.currentTimeMillis() - millisSecond;
                                MLog.d(TAG, "verify Time : " + gap);
                                mVerifyResult = true;
                                freeMemory();
                            } else {
                                handleOptFailed(CODE_VERIFY_FAILED, verify_with_sm3);
                                mVerifyResult = false;
                            }
                        }

                        @Override
                        public void onInitFailed(int errCode, int nativeCode) {
                            mVerifyResult = false;
                            handleOptFailed(errCode, nativeCode);
                        }
                    });
                } catch (Exception e) {
                    MLog.d(TAG, " verifyData data occur error:", e);
                    mVerifyResult = false;
                    handleOptFailed(CODE_DEFAULT_FAILED, -1);
                }
                return mVerifyResult;
            }
        });

        try {
            return verifyFuture.get();
        } catch (Exception e) {
            MLog.d(TAG, " sign data occur error:", e);
        }
        return false;
    }

    /**
     * 获取Y_Bit
     */
    public int getYBit(@NonNull final String userPubKey, @NonNull final String privateKey, @NonNull final String uId) {
        MLog.d(TAG, "start getYBit.....");
        mYBit = -1;
        Future<Integer> getYFuture = mExecutorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {

                    final long millisSecond = System.currentTimeMillis();
                    initCrypto(userPubKey, privateKey, uId, new InitListener() {
                        @Override
                        public void onInitSuccess() {
                            int y_bit = mCNACrypto.CNA_sm2_get_public_key_y_bit();
                            MLog.d(TAG, "CNA_sm2_get_public_key_y_bit res = " + y_bit);
                            long gap = System.currentTimeMillis() - millisSecond;
                            MLog.d(TAG, "get Y bit Time : " + gap);
                            mYBit = y_bit;
                            freeMemory();
                        }

                        @Override
                        public void onInitFailed(int errCode, int nativeCode) {
                            mYBit = -1;
                            handleOptFailed(errCode, nativeCode);
                        }
                    });
                } catch (Exception e) {
                    MLog.d(TAG, " getYBit occur error:", e);
                    mYBit = -1;
                    handleOptFailed(CODE_DEFAULT_FAILED, -1);
                }
                MLog.d(TAG, " y bit = " + mYBit);
                return mYBit;
            }
        });
        try {
            return getYFuture.get();
        } catch (Exception e) {
            MLog.d(TAG, " getYBit occur error:", e);
        }
        return -1;
    }

    private void initCrypto(@NonNull String userPubKey, @NonNull String privateKey, @NonNull String uId, @NonNull InitListener listener) {
        try {
            String keyX = userPubKey.substring(0, userPubKey.length() / 2);
            String keyY = userPubKey.substring(userPubKey.length() / 2, userPubKey.length());
            MLog.d(TAG, "keyX = " + keyX);
            MLog.d(TAG, "keyY = " + keyY);
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
            handleInitFailed(listener, CODE_DEFAULT_FAILED, -1);
        }
    }

    private void handleOptFailed(int code, int nativeCode) {
        MLog.d(TAG, "bizCode = " + code + " nativeCode = " + nativeCode);
        freeMemory();
    }

    private void handleInitFailed(@NonNull InitListener listener, int code, int nativeCode) {
        listener.onInitFailed(code, nativeCode);
        freeMemory();
    }

    private byte[] getBytesUTF8(String str) {
        byte[] value;
        try {
            value = str.getBytes("UTF-8");
            //            MLog.d(TAG,"getBytesUTF8 value = "+" org = "+str+"  "+HexConverter.bytesToHexString(value));
            return value;
        } catch (Exception e) {
            MLog.d(TAG, "getBytesUTF8 occur error:" + " org =" + str, e);
            return null;
        }
    }


    interface InitListener {
        void onInitSuccess();

        void onInitFailed(int errCode, int nativeCode);

    }
}

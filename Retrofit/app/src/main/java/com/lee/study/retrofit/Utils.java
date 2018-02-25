package com.lee.study.retrofit;


import android.os.Looper;

public class Utils {
    private static final String TAG = "Utils";

    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}

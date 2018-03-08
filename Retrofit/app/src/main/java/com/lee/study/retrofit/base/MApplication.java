package com.lee.study.retrofit.base;

import android.app.Application;
import android.util.Log;

/**
 * Created by liwx on 2018/3/5.
 */

public class MApplication extends Application {
    private static final String TAG = "MApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }
}

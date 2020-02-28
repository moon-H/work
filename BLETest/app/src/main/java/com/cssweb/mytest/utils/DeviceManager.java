package com.cssweb.mytest.utils;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by lenovo on 2016/7/28.
 */
public class DeviceManager {
    private static final String TAG = "DeviceManager";

    /**
     * 模拟按下电源键，关闭或点亮屏幕，需要系统签名
     **/
    public static void clickPowerBtn() {
        try {
            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_POWER;
            MLog.e(TAG, "keyCommand = " + keyCommand);
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        } catch (IOException e) {
            MLog.d(TAG, "clickPowerBtn occur ", e);
        }
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isInteractive()) {
            return true;
        }
        return false;
    }

    /**
     * 关机
     **/
    public static void shutDown() {
        try {

            //获得ServiceManager类
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);

            //获得IPowerManager.Stub类
            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, true);

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

}

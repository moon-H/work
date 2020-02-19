package com.cssweb.mytest.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Binder;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogUtils;
import com.cssweb.mytest.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MLog {
    private static final String TAG = "MLog";
    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "FileLog%g.txt";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    private static int flag = 0;

    //----------------------------------------------
    private Context context;

    private static boolean DEBUG = BuildConfig.DEBUG;
    //        private static boolean DEBUG = true;
    private static boolean isInit = false;

    public MLog(Context context) {
        this.context = context;
    }

    public static void init() {
        if (!isInit) {
            isInit = true;
            LogUtils.d("initLogUtils");
            LogUtils.getLogConfig().configAllowLog(true).configTagPrefix("GYSDK").configShowBorders(false).configFormatTag("%d{HH:mm:ss:SSS} %t " + "%c{-5}");
        }
    }

    public static void initWriteLog(Activity activity) {
        if (PermissionUtils.checkPermissionsGranted(activity, PermissionUtils.CustomGroup.LOG_PERMISSON)) {
            LogUtils.getLog2FileConfig().configLog2FileEnable(true)
                // targetSdkVersion >= 23 需要确保有写sdcard权限
                .configLog2FilePath("/sdcard/GYSDK/logs/").
                configLog2FileNameFormat("%d{yyyyMMdd}.txt").
                configLogFileEngine(new LogFileEngineFactory());
        }
    }

    public static void setDebugAble(boolean flag) {
        DEBUG = flag;
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            init();
            LogUtils.tag(tag).d(msg);

            //            writeLog("D", tag, msg);
            //            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            init();
            //            try {
            LogUtils.d(msg);
            //            } catch (UnsupportedEncodingException e) {
            //                e.printStackTrace();
            //            }

            //            writeLog("D", tag, msg);
            //            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {

            String error = msg + "\n" + buildException(tr);
            LogUtils.tag(tag).d(error);
            //            writeLog("D", tag, error);
            //            Log.d(tag, error);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            init();
            LogUtils.tag(tag).e(msg);

            //            writeLog("D", tag, msg);
            //            Log.d(tag, msg);
        }
    }
    //    public static void i(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("I", tag, msg);
    //            Log.i(tag, msg);
    //        }
    //    }

    //    public static void i(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("I", tag, msg);
    //            Log.i(tag, msg);
    //        }
    //    }

    //    public static void w(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("W", tag, msg);
    //            Log.w(tag, msg);
    //        }
    //    }

    //    public static void e(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("E", tag, msg);
    //            Log.e(tag, msg);
    //        }
    //    }

    //    public static void e(String tag, String msg, Throwable e) {
    //        if (DEBUG) {
    //            String error = msg + "\n" + buildException(e);
    //            writeLog("E", tag, error);
    //            Log.e(tag, error);
    //        }
    //    }

    //    public static void e(String tag, Throwable ex) {
    //        if (DEBUG) {
    //            String exception = buildException(ex);
    //            writeLog("E", tag, exception);
    //            Log.e(tag, exception);
    //        }
    //    }

    //    public static void v(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("V", tag, msg);
    //            Log.d(tag, msg);
    //        }
    //    }

    private static void writeLog(String level, String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format(level + "/%s(%d): %s\n", tag, Binder.getCallingPid(), msg));
        }
    }

    private static String buildException(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }

}

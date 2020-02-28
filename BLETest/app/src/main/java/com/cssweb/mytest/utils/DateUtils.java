package com.cssweb.mytest.utils;

import android.support.annotation.NonNull;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liwx on 2018/9/22.
 */
public class DateUtils {
    private static final String TAG = "DateUtils";

    /**
     * 16进制时间转毫秒 从2000年1月1日到当前的秒数
     */
    public static long convertDateToLong(@NonNull String dateStr) {
        try {
            String hexDate = Utils.addLeftChar(dateStr, 16, "0");
            byte[] byteDate = HexConverter.hexStringToBytes(hexDate);
            long longDate = ByteConverter.bytes2Long(byteDate);
            MLog.d(TAG, "parseDateToLong : " + longDate);
            //            Date date = mDateFormat.parse(dateStr);
            //            return date.getTime();
            return longDate;
        } catch (Exception e) {
            MLog.d(TAG, "parseDateToLong occur error:", e);
            return -1;
        }
    }

    /**
     * 2000年到现在的毫秒格式化成yyyyMMddHHmmss格式
     */
    public static String formatHandleDate(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        try {
            Date startDate = sdf.parse("20000101000000");
            long handleDate = startDate.getTime() + times;
            return sdf.format(handleDate);
        } catch (ParseException e) {
            MLog.d(TAG, "");
        }
        return "";
    }

    /**
     * 2000年到现在的毫秒格式化成yyyy-MM-dd HH:mm:ss:SSS格式
     */
    public static String formatDate2(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.CHINA);
        return sdf.format(times);
    }
}

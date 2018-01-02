package com.cssweb.mytest.javatest;

import com.cssweb.framework.utils.HexConverter;
import com.cssweb.framework.utils.Utils;
import com.cssweb.mytest.utils.ByteConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liwx on 2017/9/6.
 */

public class ZhuHai {
    private String issueCode = "01";
    private String puIdx = "01";
    private String oderNo = "2323232323232344";
    private String status = "04";
    private String type = "01";
    private String price = "0A";
    private String num = "0B";
    private String time;
    private String signData;

    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

    public static void main(String[] args) {

        String hexDate = genByteStrTime(System.currentTimeMillis());
        long date = ByteConverter.byte2Int(HexConverter.hexStringToBytes(hexDate));

        System.out.print("----hexDate = " + hexDate);
        System.out.print("----int = " + date);
        Date date1 = new Date(date * 1000);

        System.out.print("----int2 = " + mDateFormat.format(date1));
    }

    private String getHexSecondEffectiveDate() {
        int millisecond = (int) (System.currentTimeMillis() / 1000);
        System.out.println(millisecond);
        int effectiveSecond = millisecond + 7 * 24 * 60 * 60;
        System.out.println(effectiveSecond);
        byte[] b = ByteConverter.intToByte(effectiveSecond);
        String hexEffectiveSecond = HexConverter.bytesToHexString(b);
        return hexEffectiveSecond;
    }

    /**
     * 生成二维码内需要的时间格式
     */
    private static String genByteStrTime(long expireTime) {
        int localTimeInfo = (int) (expireTime / 1000);
        return Utils.addLeftChar(Integer.toHexString(localTimeInfo), 8, "0");
    }

}

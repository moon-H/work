package com.cssweb.mytest.ble;

import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cssweb.mytest.Common;
import com.cssweb.mytest.utils.ByteConverter;
import com.cssweb.mytest.utils.HexConverter;
import com.cssweb.mytest.utils.HexStringUtil;
import com.cssweb.mytest.utils.MLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by liwx on 2017/11/3.
 * <p>
 * 解析蓝牙数据
 */

public class BLEDataParser {
    private static final String TAG = "BLEDataParser";
    private static final String CUSTOM_TYPE_ENTRY = "1";//进站
    private static final String CUSTOM_TYPE_EXIT = "2";//出站
    //    private static final String CUSTOM_TYPE_OVER_TIME = "3";//超时
    private static final String CUSTOM_TYPE_OVER_TIME = "6";//超时
    private static final String CUSTOM_TYPE_OVER_JOURNEY = "4";//超乘
    private static final String CUSTOM_TYPE_OVER_JOURNEY_AND_TIME = "5";//超时+超乘

    public static final String ORG_TYPE_ENTRY = "04";//进站
    public static final String ORG_TYPE_EXIT = "05";//出站
    //    public static final String ORG_TYPE_OVER_TIME = "30";//超时
    public static final String ORG_TYPE_OVER_TIME = "06";//超时
    public static final String ORG_TYPE_OVER_JOURNEY = "31";//超乘
    public static final String ORG_TYPE_OVER_JOURNEY_AND_TIME = "32";//超时+超乘

    /***
     * 解析老广播
     * @param random 客户端随机数
     * */
    public static BroadCastData parseOldApi(byte[] adv_data, String random) {
        String advData = getOldAdvData(adv_data);
        MLog.d(TAG, "parseOldApi data = " + advData);
        return buildAdvData(advData, random);
    }

    /***
     * 解析老api获取广播数据
     * */
    private static String getOldAdvData(byte[] adv_data) {
        try {
            String advData = "";
            ByteBuffer buffer = ByteBuffer.wrap(adv_data).order(ByteOrder.LITTLE_ENDIAN);
            MLog.d(TAG, "parseData new = " + HexConverter.bytesToHexString(buffer.array()));
            while (buffer.remaining() > 2) {
                byte length = buffer.get();
                if (length == 0)
                    break;
                byte type = buffer.get();
                length -= 1;
                switch (type) {
                    case (byte) 0x16:
                        MLog.d(TAG, "UUID = " + String.format("%08x-0000-1000-8000-00805f9b34fb", buffer.getShort()));
                        byte sb2[] = new byte[length - 2];
                        buffer.get(sb2, 0, length - 2);
                        advData = HexConverter.bytesToHexString(sb2);
                        MLog.d(TAG, " CONTENT = " + advData);
                        length -= length;
                        break;
                    default: // skip
                        MLog.d(TAG, "default type = " + type);
                        break;
                }
                if (length > 0) {
                    buffer.position(buffer.position() + length);
                }
            }
            MLog.d(TAG, "adv data= " + advData);
            return advData;
        } catch (Exception e) {
            MLog.d(TAG, "");
            return null;
        }
    }


    /**
     * 解析新api的广播包，提取广播数据
     */
    public static BroadCastData parseNewApi(final ScanResult scanResult) {
        try {
            if (scanResult == null) {
                MLog.d(TAG, "scan result is null");
                return null;
            }
            String data = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                data = getAdvData(scanResult.getScanRecord());
            } else {
                MLog.d(TAG, "parseNewApi SDK is  = " + Build.VERSION.SDK_INT);
                return null;
            }
            return buildAdvData(data, "");
        } catch (Exception e) {
            MLog.d(TAG, "parseNewApi occur error::", e);
            return null;
        }
    }

    /**
     * 解析广播数据,封装BroadCastData对象 共17字节
     * 状态 1字节,车站编号 2字节,设备编号 2字节
     * 票号 4字节，交易时间 1字节,金额 1字节
     * 票号 4字节，交易时间 1字节,金额 1字节
     *
     * @param random 客户端随机数
     */
    private static BroadCastData buildAdvData(String data, String random) {
        try {
            if (TextUtils.isEmpty(data) || data.length() != 34) {
                MLog.d(TAG, "buildAdvData is invalid = " + data);
                return null;
            }

            byte[] bytesData = HexConverter.hexStringToBytes(data);
            if (bytesData != null) {
                byte[] type = Arrays.copyOfRange(bytesData, 0, 1);
                String typeStr = HexConverter.bytesToHexString(type);
                String deviceCode = HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 1, 5));
                MLog.d(TAG, "RANDOM = " + random + " encode data =" + HexConverter.bytesToHexString(bytesData));

                byte[] decodeTransData = HexStringUtil.xor(HexConverter.hexStringToBytes(random), Arrays.copyOfRange(bytesData, 5, 12));
                MLog.d(TAG, "decode data = " + HexConverter.bytesToHexString(decodeTransData));
                //----------交易1---
                byte[] orgSjtIdByte = Arrays.copyOfRange(decodeTransData, 0, 4);
                byte[] handleTimeByte1 = Arrays.copyOfRange(decodeTransData, 4, 5);
                byte[] amount1Byte = Arrays.copyOfRange(decodeTransData, 5, 6);
                byte[] sjtIdByte = new byte[4];
                System.arraycopy(orgSjtIdByte, 0, sjtIdByte, 0, orgSjtIdByte.length);
                String sjt1Type = convertCustomTypeToS5Type(typeStr.substring(0, 1));
                int sjt1Id = ByteConverter.byteToInt(HexConverter.hexStringToBytes(HexConverter.bytesToHexString(sjtIdByte)));
                //                int sjt1HandleTime = Integer.valueOf(HexConverter.bytesToHexString(handleTimeByte1), 16);
                int sjt1HandleTime = HexConverter.hexStringToByte(HexConverter.bytesToHexString(handleTimeByte1));
                int amount1 = Integer.valueOf(HexConverter.bytesToHexString(amount1Byte), 16) * 10;
                //----------交易1---

                //----------交易2---
                byte[] orgSjtId2Byte = Arrays.copyOfRange(decodeTransData, 6, 10);
                byte[] handleTimeByte2 = Arrays.copyOfRange(bytesData, 10, 11);
                byte[] sjtId2Byte = new byte[4];
                System.arraycopy(orgSjtId2Byte, 0, sjtId2Byte, 0, orgSjtId2Byte.length);
                String sjt2Type = convertCustomTypeToS5Type(typeStr.substring(1, 2));
                int sjt2Id = ByteConverter.byteToInt(sjtId2Byte);
                //                int sjt2HandleTime = Integer.valueOf(HexConverter.bytesToHexString(handleTimeByte2), 16);
                int sjt2HandleTime = HexConverter.hexStringToByte(HexConverter.bytesToHexString(handleTimeByte2));
                byte[] amount2Byte = Arrays.copyOfRange(bytesData, 11, 12);
                int amount2 = Integer.valueOf(HexConverter.bytesToHexString(amount2Byte), 16) * 10;
                //----------交易2---

                BroadCastData advData = new BroadCastData();
                advData.sjtId1 = sjt1Id;
                advData.sjtId2 = sjt2Id;
                advData.sationCode = deviceCode.substring(0, 4);
                advData.deviceCode = deviceCode.substring(4, 8);
                advData.amount1 = amount1;
                advData.amount2 = amount2;
                advData.handleTime1 = sjt1HandleTime;
                advData.handleTime2 = sjt2HandleTime;
                advData.trxType1 = sjt1Type;
                advData.trxType2 = sjt2Type;
                return advData;
            } else {
                MLog.d(TAG, "buildAdvData data is null");
                return null;
            }
        } catch (Exception e) {
            MLog.d(TAG, "buildAdvData occur error:", e);
            return null;
        }
    }


    /**
     * 获取广播数据
     */
    private static String getAdvData(ScanRecord scanRecord) {
        String data = "";
        if (scanRecord != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (scanRecord.getServiceData() != null) {
                    byte[] deviceCode = scanRecord.getServiceData().get(new ParcelUuid( Common.UUID_CHARACTERISTIC_DATA_SHARE));
                    if (deviceCode != null) {
                        data = HexConverter.bytesToHexString(deviceCode);
                        MLog.d(TAG, "findDeviceCode = " + data);
                    } else {
                        MLog.d(TAG, "getAdvData data is null or length is invalid " + " length = ");
                    }
                }
            } else
                MLog.d(TAG, "getAdvData SDK version is = " + Build.VERSION.SDK_INT);

        }
        return data;
    }

    /**
     * 半个字节自定义类型转换成S5通用类型
     */
    private static String convertCustomTypeToS5Type(@NonNull String type) {
        if (!TextUtils.isEmpty(type)) {
            if (type.equals(CUSTOM_TYPE_ENTRY)) {
                //进站
                return ORG_TYPE_ENTRY;
            } else if (type.equals(CUSTOM_TYPE_EXIT)) {
                //进站
                return ORG_TYPE_EXIT;
            } else if (type.equals(CUSTOM_TYPE_OVER_TIME)) {
                //超时
                return ORG_TYPE_OVER_TIME;
            }
            //            else if (type.equals(CUSTOM_TYPE_OVER_JOURNEY)) {
            //                //超乘
            //                return ORG_TYPE_OVER_JOURNEY;
            //            } else if (type.equals(CUSTOM_TYPE_OVER_JOURNEY_AND_TIME)) {
            //                //超乘+超时
            //                return ORG_TYPE_OVER_JOURNEY_AND_TIME;
            //            }

        }
        return null;
    }

    private static String buildStationCode(String orgDeviceCode) {
        String deviceCode = "";
        if (!TextUtils.isEmpty(orgDeviceCode)) {
            deviceCode = orgDeviceCode.substring(0, 4) + "04" + orgDeviceCode.substring(4, 8);
        }
        return deviceCode;
    }
}

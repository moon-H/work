package com.cssweb.mytest.ble;

import java.io.Serializable;

/**
 * Created by liwx on 2017/11/3.
 * 广播数据
 */

public class BroadCastData implements Serializable {
    public String sationCode;//站点编码
    public String deviceCode;//设备编号
    public int sjtId1;//票号
    public String trxType1;//交易类型
    public int handleTime1;//交易时间
    public int amount1;//交易金额
    public int sjtId2;
    public String trxType2;
    public int handleTime2;
    public int amount2;
    public String errorInfo;//异常信息

    @Override
    public String toString() {
        return "BroadCastData{" + "sationCode='" + sationCode + '\'' + ", deviceCode='" + deviceCode + '\'' + ", sjtId1=" + sjtId1 + ", trxType1='" + trxType1 + '\'' + ", handleTime1=" + handleTime1 + ", amount1=" + amount1 + ", sjtId2=" + sjtId2 + ", trxType2='" + trxType2 + '\'' + ", handleTime2=" + handleTime2 + ", amount2=" + amount2 + ", errorInfo='" + errorInfo + '\'' + '}';
    }
}

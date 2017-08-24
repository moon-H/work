package com.cssweb.mytest;

/**
 * Created by lenovo on 2016/6/4.
 */
public class ByteTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        byte x = 0x32;
        byte init = 0x15;
        byte result = 0x27;
        //        System.out.println("byte to bit = " + byteToBit(x));
        System.out.println("异或 = " + (x ^ init));
        System.out.println("byte 值 = " + (result & 0xff));
        byte[] org = {(byte) 0x32, (byte) 0x34, 0x36, 0x38, 0x30, 0x62, 0x64, 0x66};
        byte initValue = 0x15;
        byte[] convertTable = {0x01, 0x05, 0x07, 0x09, 0x02, 0x04, 0x03, 0x00, 0x0C, 0x0F, 0x0B, 0x0D, 0x08, 0x0E, 0x0A, 0x06};
        //        System.out.println("int 转 byte = " +iniVaue );
        byte[] resultArray = new byte[org.length];
        for (int i = 0; i < org.length; i++) {
            initValue = (byte) (org[i] ^ initValue);
            resultArray[i] = initValue;
        }
        System.out.println("结果 = " + HexConverter.bytesToHexString(resultArray));
        String hex = Integer.toBinaryString(10);
        System.out.println("hex = " + hex);
        byte temp = 0x27;
        System.out.println("bit = " + byteToBit(temp));
        System.out.println("int  = " + Integer.valueOf( "111",2));
    }

    public static String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

}

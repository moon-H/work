package com.cssweb.mytest.qrcodejni;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密、解密
 *
 * @author
 * @ClassName PostPayDES
 * @date
 */
public class DES {

    /**
     * DES加密
     *
     * @param HexString 字符串（16位16进制字符串）
     * @param keyStr    密钥16个1
     * @throws Exception
     */
    public static byte[] SEncrypt_DES(byte[] HexString, byte[] keyStr) throws Exception {
        try {
            byte[] theCph = new byte[8];
            try {
                byte[] theKey = null;
                byte[] theMsg = null;
                theMsg = HexString;
                theKey = keyStr;
                KeySpec ks = new DESKeySpec(theKey);
                SecretKeyFactory kf = SecretKeyFactory.getInstance("PostPayDES");
                SecretKey ky = kf.generateSecret(ks);
                Cipher cf = Cipher.getInstance("PostPayDES/ECB/NoPadding");
                cf.init(Cipher.ENCRYPT_MODE, ky);
                theCph = cf.doFinal(theMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return theCph;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * DES解密
     *
     * @param hexStr 16位十六进制字符串
     * @param keyStr 密钥16个1
     *               解密模式:ECB
     * @throws Exception
     */
    public static byte[] SDecrypt_DES(byte[] hexStr, byte[] keyStr) throws Exception {
        try {
            String algorithm = "PostPayDES/ECB/NoPadding";
            byte[] theCph = new byte[8];
            byte[] theKey = null;
            byte[] theMsg = null;
            theMsg = hexStr;
            theKey = keyStr;
            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PostPayDES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance(algorithm);
            cf.init(Cipher.DECRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            return theCph;

        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }

    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16)
                    str = str + "0" + Integer.toHexString(data[i] & 0xFF);
                else
                    str = str + Integer.toHexString(data[i] & 0xFF);
            }
            return str.toUpperCase();
        }
    }

    public static void main(String[] args) throws Exception {
        String key = "FFFFFFFFFFFFFFFF";
        String data = "1111111111111111";
        byte[] miwen = DES.SEncrypt_DES(hexToBytes(data), hexToBytes(key));
        System.out.println("密文：" + bytesToHex(miwen));
        byte[] plainDate = DES.SDecrypt_DES(miwen, hexToBytes(key));
        System.out.println("明文：" + bytesToHex(plainDate));
    }

}
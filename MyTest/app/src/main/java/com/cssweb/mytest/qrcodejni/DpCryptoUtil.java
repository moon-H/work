package com.cssweb.mytest.qrcodejni;

import com.cssweb.framework.utils.HexConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 *
 * @author GCY
 */
public class DpCryptoUtil {

    //初始化离散表
    public static final byte[] TRANS_TABLE = {0x1E, 0x13, 0x17, 0x11, 0x09, 0x03, 0x02, 0x16, 0x01, 0x08, 0x0D, 0x10, 0x14, 0x1B, 0x0C, 0x04, 0x07, 0x0B, 0x19, 0x1C, 0x0E, 0x12, 0x0A, 0x1D, 0x00, 0x05, 0x06, 0x1F, 0x0F, 0x15, 0x18, 0x1A};

    /**
     * 连续异或算法 加密
     *
     * @param data
     * @return
     */
    public static byte[] encodeContinuousXOR(byte[] data, byte init) {
        byte[] encodeCode = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            init = (byte) (init ^ data[i]);
            encodeCode[i] = init;
        }
        return encodeCode;
    }

    /**
     * 连续异或算法
     *
     * @param data
     * @return
     */
    public static byte continuousXOR(byte[] data, byte init) {
        for (int i = 0; i < data.length; i++) {
            init = (byte) (init ^ data[i]);
        }
        return init;
    }

    /**
     * 连续异或算法 解密
     *
     * @param data
     * @param init
     * @return
     */
    public static byte[] decodeContinuousXOR(byte[] data, byte init) {
        byte[] decodeCode = new byte[data.length];
        for (int i = data.length - 1; i > 0; i--) {
            decodeCode[i] = (byte) (data[i] ^ data[i - 1]);
        }
        decodeCode[0] = (byte) (data[0] ^ init);
        return decodeCode;
    }

    /**
     * 表定义分散算法 加密
     *
     * @param data       加密数据
     * @param transTable 定义表
     * @return
     * @throws Exception
     */
    public static byte[] encodeDiversify(byte[] data, byte[] transTable) throws Exception {

        // 定义表中指针与加密数据顺序对应关系
        Map<String, Integer> transMap = new HashMap<String, Integer>();
        for (int i = 0; i < transTable.length; i++) {
            transMap.put(HexConverter.bytesToHexString(new byte[]{transTable[i]}), i);
        }

        // 确定加密数据中每个字符对应定义表中的位置，重排
        String dataHex = HexConverter.bytesToHexString(data);
        char[] c = new char[dataHex.length()];
        for (int i = 0; i < dataHex.length(); i++) {
            String mapKey = addLeftZero(Integer.toHexString(i), 2).toUpperCase();
            Integer index = transMap.get(mapKey);
            c[index] = dataHex.charAt(i);
        }
        return parseHexString(new String(c));
    }

    /**
     * 表定义分散算法 解密
     *
     * @param data
     * @param transTable
     * @return
     * @throws Exception
     */
    public static byte[] decodeDiversify(byte[] data, byte[] transTable) throws Exception {
        // 定义表中指针与加密数据顺序对应关系
        Map<Integer, Byte> transMap = new HashMap<Integer, Byte>();
        for (int i = 0; i < transTable.length; i++) {
            transMap.put(i, transTable[i]);
        }
        String dataHex = HexConverter.bytesToHexString(data);
        char[] c = new char[dataHex.length()];
        for (int i = 0; i < dataHex.length(); i++) {
            Integer index = Integer.parseInt(HexConverter.bytesToHexString(new byte[]{transMap.get(i)}), 16);
            c[index] = dataHex.charAt(i);
        }
        return parseHexString(new String(c));
    }

    /**
     * Mac加密
     *
     * @param data 加密数据
     * @param init 初始值
     * @return
     * @throws Exception
     */
    public static byte[] encodeMac(byte[] data, byte[] init) throws Exception {
        byte[] key = init;
        for (int i = 0; i < data.length; i += 8) {
            // 长度=8字节
            if (i + 8 == data.length) {
                byte[] tail = {(byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                byte[] code = new byte[8];
                System.arraycopy(data, i, code, 0, 8);
                key = DES.SEncrypt_DES(data, key);
                key = DES.SEncrypt_DES(tail, key);
            } else if (i + 8 > data.length) {
                int length = data.length - i;
                byte[] code = new byte[8];
                System.arraycopy(data, i, code, 0, length);
                for (int j = 0; j < 8 - length; j++) {
                    if (j == 0) {
                        code[length + j] = (byte) 0x80;
                    } else {
                        code[length + j] = 0x00;
                    }
                }
                key = DES.SEncrypt_DES(code, key);
            } else {
                byte[] code = new byte[8];
                System.arraycopy(data, i, code, 0, 8);
                key = DES.SEncrypt_DES(code, key);
            }
        }
        byte[] mac = new byte[4];
        System.arraycopy(key, 0, mac, 0, 4);
        return mac;
    }

    public static byte[] hideData(byte[] data) {
        byte init = 0x15;
        byte[] res = encodeContinuousXOR(data, init);
        byte[] resDiver = null;
        try {
            System.out.println(HexConverter.bytesToHexString(res));
            resDiver = encodeDiversify(res, TRANS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resDiver;
    }

    public static byte[] showData(String hexData) {
        byte[] data = HexConverter.hexStringToBytes(hexData);
        byte init = 0x15;
        byte[] resDiver = null;
        byte[] res = null;
        try {
            //            System.out.println(HexConverter.bytesToHexString(res));
            resDiver = decodeDiversify(data, TRANS_TABLE);
            res = decodeContinuousXOR(resDiver, init);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] showData(byte[] data) {
        byte init = 0x15;
        byte[] resDiver = null;
        byte[] res = null;
        try {
            //            System.out.println(HexConverter.bytesToHexString(res));
            resDiver = decodeDiversify(data, TRANS_TABLE);
            res = decodeContinuousXOR(resDiver, init);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字符串左补0
     *
     * @param param
     * @param size
     * @return
     * @throws Exception
     */
    public static String addLeftZero(String param, int size) {
        if (size <= param.length()) {
            return param;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= size - param.length(); i++) {
            sb.append("0");
        }
        sb.append(param);
        return sb.toString();
        //		return String.format("%0" + size + "d", param);
    }

    /**
     * Convert input HexStrings to Byte[].
     */
    public static byte[] parseHexString(String s) throws Exception {
        int stringLen = s.length();
        byte[] temp = new byte[stringLen];
        int resultLength = 0;
        int nibble = 0;
        byte nextByte = 0;
        for (int i = 0; i < stringLen; i++) {
            char c = s.charAt(i);
            byte b = (byte) Character.digit(c, 16);
            if (b == -1) {
                if (!Character.isWhitespace(c))
                    throw new Exception("Not HexString character: " + c);
                continue;
            }
            if (nibble == 0) {
                nextByte = (byte) (b << 4);
                nibble = 1;
            } else {
                nextByte |= b;
                temp[resultLength++] = nextByte;
                nibble = 0;
            }
        }// for
        if (nibble != 0) {
            throw new Exception("Not enough characters to fill in a byte");
        } else {
            byte[] result = new byte[resultLength];
            System.arraycopy(temp, 0, result, 0, resultLength);
            return result;
        }
    }

}

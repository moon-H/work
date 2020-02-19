/*
 * @(#)HexStringUtil.java
 * Copyright (c) 2004 Snow, Inc. All Rights Reserved.
 *
 * �ۼ����� : 2006. 3. 7.
 *
 * ���� ���� :
 * ����		����		�ۼ���		�������
 *
 *
 */
package com.cssweb.mytest.utils;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * ���� :
 *
 * @author : Daeman Kwon
 * @version : 1.0
 */

public class HexStringUtil {

    private static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * Convert input Byte[] to HexString from offset as length including space.
     */
    public static String byteToHexString(byte d[], int offset, int len) {
        String result = new String();
        int to = offset + len;
        for (int i = offset; i < to; i++)
            result = result + (hexifyByte(d[i]) + " ");
        return result;
    }

    /**
     * Convert input Byte[] to HexString from offset as length.
     */
    public static String byteToHexString(byte d[], int offset, int len, boolean spaced) {
        String result = new String();
        int to = offset + len;
        for (int i = offset; i < to; i++) {
            result = result + hexifyByte(d[i]);
            if (spaced)
                result = result + " ";
        }
        return result;
    }

    /**
     * Convert input Byte[] to HexString.
     */
    public static String byteToHexString(byte[] d) {
        return new String(encodeHex(d));
        //		return byteToHexString(d, 0, d.length, false);
    }

    /**
     * Convert input Byte[] to HexString.
     */
    public static String byteToHexString(byte[] d, boolean spaced) {
        return byteToHexString(d, 0, d.length, spaced);
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
        }//for
        if (nibble != 0) {
            throw new Exception("Not enough characters to fill in a byte");
        } else {
            byte[] result = new byte[resultLength];
            System.arraycopy(temp, 0, result, 0, resultLength);
            return result;
        }
    }

    public static byte[] asciiToByte(String str)
    //	"ABC..." -> [41,42,43,..]
    {
        if (str == null || str.length() == 0)
            return new byte[0];

        int len = str.length();
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++)
            data[i] = (byte) str.charAt(i);
        return data;
    }

    public static byte[] leftPaddingBytes(byte[] buffer, byte padding, byte[] source) throws Exception {
        if (buffer.length <= source.length)
            throw new Exception("Not enough buffer to fill in a padding byte");
        int paddingLength = buffer.length - source.length;

        byte[] padBlock = new byte[paddingLength];

        for (int i = 0; i < paddingLength; i++) {
            padBlock[i] = padding;
        }

        System.arraycopy(padBlock, 0, buffer, 0, paddingLength);
        System.arraycopy(source, 0, buffer, paddingLength, source.length);
        return buffer;
    }

    public static byte[] rightPaddingBytes(byte[] buffer, byte padding, byte[] source) throws Exception {
        if (buffer.length <= source.length)
            throw new Exception("Not enough buffer to fill in a padding byte");
        int paddingLength = buffer.length - source.length;

        byte[] padBlock = new byte[paddingLength];

        for (int i = 0; i < paddingLength; i++) {
            padBlock[i] = padding;
        }

        System.arraycopy(source, 0, buffer, 0, source.length);
        System.arraycopy(padBlock, 0, buffer, source.length, paddingLength);

        return buffer;
    }

    public static final int bytesToSingedInt(byte abyte0[], int offset, int length) {
        int result = 0;
        for (int i = 0; i < length; i++)
            result = result << 8 | abyte0[i + offset] & 0xff;

        return result;
    }

    public static final int bytesToSingedInt(byte abyte0[]) {
        return bytesToSingedInt(abyte0, 0, abyte0.length);
    }


    /**
     * Convert integer value to hexadecimal byte presentation
     *
     * @param val Value
     * @return 2 digit hexadecimal string
     */
    public static String hexifyByte(int val) {
        return "" + digits[(val >>> 4) & 0x0F] + digits[val & 0x0F];
    }

    /**
     * Convert byte value to hexadecimal byte presentation
     *
     * @param val Value
     * @return 2 digit hexadecimal string
     */
    public static String hexifyByte(byte val) {
        return hexifyByte((int) val & 0xFF);
    }

    /**
     * Convert int value to hexadecimal short presentation
     *
     * @param val Value
     * @return 4 digit hexadecimal string
     */
    public static String hexifyShort(int val) {
        return hexifyByte((val >>> 8) & 0xFF) + hexifyByte(val & 0xFF);
    }


    /**
     * Convert int value to hexadecimal int presentation
     *
     * @param val Value
     * @return 8 digit hexadecimal string
     */
    public static String hexifyInt(int val) {
        return hexifyShort((val >> 16) & 0xFFFF) + hexifyShort(val & 0xFFFF);
    }

    /**
     * Compares an array from the specified source array
     *
     * @param src     source byte array.
     * @param srcOff  offset within source byte array to start compare.
     * @param dest    destination byte array.
     * @param destOff offset within destination byte array to start compare.
     * @param len     byte length to be compared.
     * @return boolean result of the comparison
     */
    public static boolean arrayCompare(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
        for (int i = 0; i < len; i++)
            if (src[srcOff + i] != dest[destOff + i])
                return false;
        return true;
    }

    /**
     * Compares an array from the specified source array
     *
     * @param src  source byte array.
     * @param dest destination byte array.
     * @return boolean result of the comparison
     */
    public static boolean arrayCompare(byte[] src, byte[] dest) {
        int srcLen = src.length;
        if (srcLen != dest.length)
            return false;
        else
            return arrayCompare(src, 0, dest, 0, srcLen);
    }

    /**
     * Dump buffer in hexadecimal format with offset and character codes
     *
     * @param data   Byte buffer
     * @param offset Offset into byte buffer
     * @param length Length of data to be dumped
     * @param widths Number of bytes per line
     * @param indent Number of blanks to indent each line
     * @return String containing the dump
     */
    public static String dump(byte[] data, int offset, int length, int widths, int indent) {
        StringBuffer buffer = new StringBuffer(80);
        int i, ofs, len;
        char ch;

        if ((data == null) || (widths == 0) || (length < 0) || (indent < 0))
            throw new IllegalArgumentException();

        while (length > 0) {
            for (i = 0; i < indent; i++)
                buffer.append(' ');

            buffer.append(hexifyShort(offset));
            //buffer.append("  ");
            buffer.append("h: ");

            ofs = offset;
            len = widths < length ? widths : length;

            for (i = 0; i < len; i++, ofs++) {
                buffer.append(digits[(data[ofs] >>> 4) & 0x0F]);
                buffer.append(digits[data[ofs] & 0x0F]);
                buffer.append(' ');
            }

            for (; i < widths; i++) {
                buffer.append("   ");
            }

            buffer.append(' ');
            ofs = offset;

            for (i = 0; i < len; i++, ofs++) {
                ch = (char) (data[ofs] & 0xFF);
                if ((ch < 32) || ((ch >= 127) && (ch <= 0xA0)))
                    ch = '.';
                buffer.append(ch);
            }

            buffer.append('\n');

            offset += len;
            length -= len;
        }
        return buffer.toString();
    }


    /**
     * Dump buffer in hexadecimal format with offset and character codes
     *
     * @param data   Byte buffer
     * @param offset Offset into byte buffer
     * @param length Length of data to be dumped
     * @param widths Number of bytes per line
     * @return String containing the dump
     */
    public static String dump(byte[] data, int offset, int length, int widths) {
        return dump(data, offset, length, widths, 0);
    }


    /**
     * Dump buffer in hexadecimal format with offset and character codes.
     * Output 16 bytes per line
     *
     * @param data   Byte buffer
     * @param offset Offset into byte buffer
     * @param length Length of data to be dumped
     * @return String containing the dump
     */
    public static String dump(byte[] data, int offset, int length) {
        return dump(data, offset, length, 16, 0);
    }


    /**
     * Dump buffer in hexadecimal format with offset and character codes
     *
     * @param data Byte buffer
     * @return String containing the dump
     */
    public static String dump(byte[] data) {
        return dump(data, 0, data.length, 16, 0);
    }

    /**
     * Bitwise XOR between corresponding bytes
     *
     * @param op1 byteArray1
     * @param op2 byteArray2
     * @return an array of length = the smallest between op1 and op2
     */
    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] result = null;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        } else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    public static byte[] longToByteArray(long input, int arrayLength) throws Exception {
        String tmp = Long.toHexString(input);
        if (tmp.length() % 2 != 0)
            tmp = "0" + tmp;
        byte[] tmpbuf = new byte[0];
        tmpbuf = HexStringUtil.parseHexString(tmp);

        if (tmpbuf.length > arrayLength)
            throw new Exception("input arrayLength small than output Array");

        byte[] buf = new byte[arrayLength];
        System.arraycopy(tmpbuf, 0, buf, arrayLength - tmpbuf.length, tmpbuf.length);

        return buf;
    }

    public static final BitSet bytesToBitSet(byte[] b) {
        int length = 8 * b.length;
        BitSet bs = new BitSet(length);
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < 8; j++) {
                //bs.set((b<0) ? 1 : 0);
                if (b[i] < 0)
                    bs.set((i * 8) + j + 1);
                else
                    bs.clear((i * 8) + j + 1);
                b[i] <<= 1;
            }
        }
        return bs;
    }

    public static final BitSet byteToBitSet(byte b) {
        BitSet bs = new BitSet(8);
        for (int i = 0; i < 8; i++) {
            //bs.set((b<0) ? 1 : 0);

            if (b < 0)
                bs.set(i + 1);
            else
                bs.clear(i + 1);

            b <<= 1;
        }

        return bs;
    }

    public static byte[] bigIntegerToBytes(BigInteger big) {
        //BigInteger it = new BigInteger();

        byte[] bigBytes = big.toByteArray();

        if ((big.bitLength() % 8) == 0 && bigBytes.length != big.bitLength() / 8) {
            byte[] smallerBytes = new byte[big.bitLength() / 8];
            System.arraycopy(bigBytes, 1, smallerBytes, 0, smallerBytes.length);
            return smallerBytes;
        } else {
            return bigBytes;
        }
    }

    public static String leftZeroPad(String input, int digitNumber) {
        String out = "";
        if (input != null)
            out = input;
        if (digitNumber > out.length())
            for (; out.length() < digitNumber; out = "0" + out)
                ;
        return out.toUpperCase();
    }

    public static String rightZeroPad(String input, int digitNumber) {
        String out = "";
        if (input != null)
            out = input;
        if (digitNumber > out.length())
            for (; out.length() < digitNumber; out = out + "0")
                ;
        return out.toUpperCase();
    }

    public static final byte[] intToBytes(int i) {
        int j = (Integer.toHexString(i).length() + 1) / 2;
        byte abyte0[] = new byte[j];
        for (int k = 0; k < j; k++)
            abyte0[k] = (byte) (i >>> 8 * (j - 1 - k) & 0xff);

        return abyte0;
    }

    /**
     * Concatenates two byte arrays (array1 and array2)
     *
     * @param array1
     * @param beginIndex1
     * @param length1
     * @param array2
     * @param beginIndex2
     * @param length2
     * @return the concatenated array
     */

    public static byte[] concat(byte[] array1, int beginIndex1, int length1, byte[] array2, int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return concatArray;
    }

    public static byte[] concat(byte[] array1, byte[] array2) {
        return concat(array1, 0, array1.length, array2, 0, array2.length);
    }

    public static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = digits[(0xF0 & data[i]) >>> 4];
            out[j++] = digits[0x0F & data[i]];
        }

        return out;
    }
}

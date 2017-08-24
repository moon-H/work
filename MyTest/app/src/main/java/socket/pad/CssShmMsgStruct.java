package socket.pad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: wangjf
 * Date: 2016-3-10
 * Time: 2:34:03
 * To change this template use File | Settings | File Templates.
 */
public class CssShmMsgStruct {
    public static final int PACKED_FIX_SIZE = 96;
    public int iAppId = 0;            /*!<数据源应用程序ID*/
    public int iDestAppId = 0;        /*!<目的应用程序ID*/
    public int iSeqId = 0;            /*!<序列号,对于指定的请求要求回复与请求序列号一致*/
    public int iNetFlag = 0;            /*!<交换标志(0表示发送ENQ,1表示回复ACK消息,2表示超时消息)*/
    public int iTimeOut = 0;            /*!时间戳 <超时标志(如果为0表示不进行超时,其它值表示超时时间秒)*/
    //int  			 	iSendimeStamp;			/*!时间戳 */
    public boolean bNeedAck = false;            /*!<ENQ是否需要ACK标志>*/
    public int iDataLen = 0;            /*!<数据包的长度(包括该包头)*/
    public byte[] szReserved = new byte[32];    /*!<保留字段*/
    public CssProcMsgStruct stProcMsg = new CssProcMsgStruct();        /*!<实际数据*/

    public static byte[] pack(CssShmMsgStruct stMsg) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(5 * 1024);

        // Count Size.
        stMsg.iDataLen = PACKED_FIX_SIZE;
        stMsg.stProcMsg.iAppendArgLen = 0;
        byte[] pData = null;

        if (stMsg.stProcMsg.pszParamData != null) {
            pData = stMsg.stProcMsg.pszParamData.getBytes("UTF-8");

            stMsg.stProcMsg.iAppendArgLen = pData.length;
            stMsg.iDataLen += pData.length;
        }

        writeNumberToStream(out, stMsg.iAppId, 4);
        writeNumberToStream(out, stMsg.iDestAppId, 4);
        writeNumberToStream(out, stMsg.iSeqId, 4);
        writeNumberToStream(out, stMsg.iNetFlag, 4);

        writeNumberToStream(out, stMsg.iTimeOut, 4);
        writeNumberToStream(out, stMsg.bNeedAck ? 1 : 0, 4);
        writeNumberToStream(out, stMsg.iDataLen, 4);
        out.write(stMsg.szReserved, 0, 32);

        writeNumberToStream(out, stMsg.stProcMsg.iCmd, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iAppendArgLen, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam0, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam1, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam2, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam3, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam4, 4);
        writeNumberToStream(out, stMsg.stProcMsg.iParam5, 4);
        // in C++, a char * pointer has 4 bytes.
        writeNumberToStream(out, 0, 4);
        // Write Data.
        if (pData != null && pData.length > 0)
            out.write(pData);

        return out.toByteArray();
    }

    public static void writeNumberToStream(OutputStream out, long number, int bytesCount) throws IOException {
        for (int i = 0; i < bytesCount; i++) {
            out.write((byte) ((number >> (i * 8)) & 0xff));
        }
    }

    public static void numberToBytes(long number, byte[] buf, int startIndex, int bytesCount) {
        for (int i = 0; i < bytesCount; i++) {
            buf[startIndex + i] = (byte) ((number >> (i * 8)) & 0xff);
        }
    }

    public static long bytesToNumber(byte[] buf, int startIndex, int bytesCount) {
        long number = 0;
        for (int i = 0; i < bytesCount; i++) {
            number = number | ((buf[startIndex + i] & 0xFF) << (i * 8));
        }
        return number;
    }

    public static CssShmMsgStruct makeMsgStruct(int mAppid, int mDestModuleId, int nMsgId, int iParam0, int iParam1, int iParam2, int iParam3, int iParam4, int iParam5, int iParamDataLength, String pszParamData) {
        CssShmMsgStruct stMsg = new CssShmMsgStruct();

        stMsg.iAppId = mAppid;
        stMsg.iDestAppId = mDestModuleId;
        stMsg.iTimeOut = 0;
        stMsg.stProcMsg.iCmd = nMsgId;

        stMsg.stProcMsg.iParam0 = iParam0;
        stMsg.stProcMsg.iParam1 = iParam1;
        stMsg.stProcMsg.iParam2 = iParam2;
        stMsg.stProcMsg.iParam3 = iParam3;
        stMsg.stProcMsg.iParam4 = iParam4;
        stMsg.stProcMsg.iParam5 = iParam5;

        stMsg.stProcMsg.pszParamData = pszParamData;

        return stMsg;
    }

    public static CssShmMsgStruct unpack(byte[] buf) throws IOException {
        CssShmMsgStruct stMsg = new CssShmMsgStruct();
        int index = 0;
        stMsg.iAppId = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.iDestAppId = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.iSeqId = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.iNetFlag = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.iTimeOut = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.bNeedAck = (bytesToNumber(buf, index, 4) != 0);
        index += 4;

        stMsg.iDataLen = (int) bytesToNumber(buf, index, 4);
        index += 4;

        System.arraycopy(buf, index, stMsg.szReserved, 0, 32);
        index += 32;

        stMsg.stProcMsg.iCmd = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iAppendArgLen = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam0 = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam1 = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam2 = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam3 = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam4 = (int) bytesToNumber(buf, index, 4);
        index += 4;
        stMsg.stProcMsg.iParam5 = (int) bytesToNumber(buf, index, 4);
        index += 4;

        // Skip char * pszParamData
        bytesToNumber(buf, index, 4);
        index += 4;

        if (stMsg.stProcMsg.iAppendArgLen == 0)
            stMsg.stProcMsg.pszParamData = "";
        else {
            stMsg.stProcMsg.pszParamData = new String(buf, index, stMsg.stProcMsg.iAppendArgLen, "UTF-8");
        }

        return stMsg;
    }

    @Override
    public String toString() {
        return "CssShmMsgStruct{" +
            "iAppId=" + iAppId +
            ", iDestAppId=" + iDestAppId +
            ", iSeqId=" + iSeqId +
            ", iNetFlag=" + iNetFlag +
            ", iTimeOut=" + iTimeOut +
            ", bNeedAck=" + bNeedAck +
            ", iDataLen=" + iDataLen +
            ", szReserved=" + Arrays.toString(szReserved) +
            ", stProcMsg=" + stProcMsg +
            '}';
    }
}

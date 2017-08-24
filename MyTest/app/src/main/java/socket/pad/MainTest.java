package socket.pad;

import com.cssweb.mytest.HexConverter;

import java.io.IOException;
import java.io.OutputStream;

import socket.pad.common.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: wangjf
 * Date: 2016-3-10
 * Time: 8:24:05
 * To change this template use File | Settings | File Templates.
 */
public class MainTest {

    public static final String SJT_Adult_Entry = "{TxnType=1, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0100, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String MTR_ADULT = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0200, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String MTR_STUDENT = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0201, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String MTR_Free = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0202, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String MTR_Discount = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0203, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String MTR_Mobile = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=258, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String Daily_Ticket = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0320, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String EP = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0700, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_NORMAL = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0903, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_MONTHLY = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0906, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_Memorial = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0913, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_FREE = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0941, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_Discount = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0942, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_STUDENT = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0956, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_Pension_Ticket = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0971, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_Pension_SPara = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0972, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";
    public static final String OCT_Pension_Para = "{TxnType=2, TxnResultCode=0, TxnDetailCode='C44', CardPhyType=1, CardIssuerId='123123', CardSN='1111111111', ProductTypeId=0973, ProductCategory=1, ProductExpireDate='201603191533', ProductClass=3, PassengerType=11, LanguageId=1, ProductRemainmingValue=9900, TransactionValue=500, TransactionDiscountValue=1}";

    private static String currentTicket = MTR_Mobile;
    public static final int GATE_SENSOR_TEST = 0x093013;//传感器测试

    public static void main(String[] argvs) throws InterruptedException {

        byte[] test = new byte[4];
        int number = 0x01020304;
        System.out.println("number = " + number);
        CssShmMsgStruct.numberToBytes(number, test, 0, 4);
        number = (int) CssShmMsgStruct.bytesToNumber(test, 0, 4);

        System.out.println("number = " + Integer.parseInt("091210", 16));

        SocketProxy sp0 = new SocketProxy(1, 1, "127.0.0.1", 8001, new SocketProxyMsgHandler() {
            public int OnMsgEnq(SocketProxy sp, CssShmMsgStruct msg) throws Exception {
                switch (msg.stProcMsg.iCmd) {
                    case SocketProxy.MSG_SYS_MSG_QUEUE_CREATED:
                        //                        CssShmMsgStruct ack = sp.MsgSendEnq(msg.iAppId, 1, 0, 0, 0, 0, 0, 0, 12, "ABCD 123456!");
                        System.out.println("MSG_SYS_MSG_QUEUE_CREATED msg.iAppId = " + msg.iAppId + " param1 = " + msg.stProcMsg.iParam1);
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091210, 0, 0, 0, 0, 0, 0, 12, DataSource.ModeSetting_chuzhan_zhishuaka());
                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091210, 0, 0, 0, 0, 0, 0, 12, DataSource.ModeSetting_chuzhan_zhishuaka());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091213, 0, 0, 0, 0, 0, 0, 12, DataSource.Gate_Status());
                        //                        Thread.sleep(2000);
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091210, 0, 0, 0, 0, 0, 0, 12, DataSource.ModeSetting_weixiu());
//                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(3000);
                            sp.MsgSendOnlyEnq(msg.iAppId, 0x091210, 0, 0, 0, 0, 0, 0, 12, DataSource.ModeSetting_jinzhan_qingshuaka());
//                        }
                        //                        for (int i = 0; i < 100; i++) {
                        //                            Thread.sleep(1000);
                        //                            //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091210, 0, 0, 0, 0, 0, 0, 12, DataSource.ModeSetting_jinji());
                        //                            sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT2());
                        //
                        //                        }


                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        Thread.sleep(1000);
                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091213, 0, 0, 0, 0, 0, 0, 12, DataSource.Gate_Status());
                        //                        //                        //-------------------------------------------
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        //                        Thread.sleep(1000);
                        //                        //                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_EXIT());
                        //                        sp.MsgSendOnlyEnq(msg.iAppId, 0x091211, 0, 0, 0, 0, 0, 0, 12, DataSource.Tran_ENTRY());
                        break;
                    case SocketProxy.MSG_SYS_MSG_QUEUE_REMOVED:
                        break;
                    case GATE_SENSOR_TEST:
                        System.out.println("receive sensor test");
                        sp.MsgSendOnlyEnq(msg.iAppId, 0x093014, 0, 0, 0, 0, 0, 0, 12, "{SensorCount:24,SensorStatusList =[0,1,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0]}");

                        break;
                    default:
                        break;
                }

                return 0;
            }
        });
        sp0.startup();

/*

        SocketProxy sp = new SocketProxy(56, 1, "127.0.0.1", 8001, new SocketProxyMsgHandler() {
            public int OnMsgEnq(SocketProxy sp, CssShmMsgStruct msg) throws Exception {
                switch (msg.stProcMsg.iCmd) {
                    case 1 :
                      System.out.println("ENQ - iCmd: " + msg.stProcMsg.iCmd +
                                            ", paramData:[" + msg.stProcMsg.pszParamData + "]");
                       sp.MsgSendAck(msg, 123, 0, 0, 0, 0, 0, 12, "ABCD 123456!fafasdfas");

                        break;
                    case SocketProxy.MSG_SYS_MSG_QUEUE_CREATED :
                        break;

                    case SocketProxy.MSG_SYS_MSG_QUEUE_REMOVED :
                        break;
                    default :
                        break;
                }

                return 0;
            }
        });

        sp.startup();
*/
        //        int i = 0;
        //        while (true) {
        //            if (i == 20)
        //                break;
        //            try {
        //                sp0.MsgSendOnlyEnq(120, 0x091211, 0, 0, 0, 0, 0, 0, 12, SJT_Adult_Entry);
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //            }
        //            Thread.sleep(1000);
        //            i++;
        //        }
        //        Thread.currentThread().join();
    }

    public static byte[] intToByte(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i >>> 24);
        b[1] = (byte) (i >>> 16);
        b[2] = (byte) (i >>> 8);
        b[3] = (byte) i;
        return b;
    }

    public static void writeNumberToStream(OutputStream out, long number, int bytesCount) throws IOException {
        for (int i = 0; i < bytesCount; i++) {
            //            Log.d("123", "value = " + HexConverter.byteToHexString((byte) ((number >> (i * 8)) & 0xff)));
            System.out.println("------------" + HexConverter.byteToHexString((byte) ((number >> (i * 8)) & 0xff)));

        }
    }

    private static String getModeSettings1() {
        //进站 正常模式
        String str = "{CityCode=99, StationCode='01', DeviceId='11', StationModeId=2, StationModeStatus=2, EntryModeId=0, ExitModeId=1, TicketRecycledStatus=3, DeviceStatusCodeList=[11,22], VersionList=[1.0,2.0], LocalDeviceType=11}";
        return str;
    }

    private static String getModeSettings2() {
        //禁止进站
        String str = "{CityCode=99, StationCode='01', DeviceId='11', StationModeId=2, StationModeStatus=2, EntryModeId=1, ExitModeId=1, TicketRecycledStatus=3, DeviceStatusCodeList=[11,22], VersionList=[1.0,2.0], LocalDeviceType=11}";
        return str;
    }

    private static String getGateStatus() {
        String str = "{GateEntryStatus=1, GateExitStatus=0, GateEntrySignalCount=2, GateExitSignalCount=2}";
        return str;
    }

}

package com.cssweb.mytest.ble.scandevice;

import android.os.Environment;

import java.util.UUID;

/**
 * Created by liwx on 2016/3/16.
 */
public class Common {
    public static final String CITY_CODE_GUANGZHOU = "4401";
    public static final String CITY_CODE_SHENZHEN = "5180";
    public static final String ACTION_START_QRSJT_PERIPHERAL_BROADCAST = "com.cssweb.cloudag.ACTION_START_QRSJT_PERIPHERAL_BROADCAST";

    public static final int MSG_START_SCAN_DEVICE = 101;
    public static final int MSG_STOP_SCAN_DEVICE = 102;

    public static final int MSG_START_HEART_BEAT = 103;
    public static final int MSG_STOP_HEART_BEAT = 104;

    public static final UUID UUID_SERVICE_ENTRY_GATE = UUID.fromString("000001FA-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_EXIT_GATE = UUID.fromString("000001FB-0000-1000-8000-00805f9b34fb");


    //    public static final UUID UUID_SERVICE_ENTRY_GATE = UUID.fromString("516d165d-8c96-4dda-83c0-9f1f21cff966");
    //    public static final UUID UUID_SERVICE_EXIT_GATE = UUID.fromString("386a4aed-1bcb-4aac-a599-718f78f7d27e");
    //    public static final UUID UUID_CHARACTERISTIC_DATA_SHARE = UUID.fromString("bcd14aaa-f3ff-4d93-ad47-2351e0916892");//用于广播设备编号
    public static final UUID UUID_CHARACTERISTIC_TRANSACTION = UUID.fromString("000001FD-0000-1000-8000-00805f9b34fb");//用于广播交易数据

    public static final UUID UUID_CHARACTERISTIC_DATA_SHARE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");//用于广播设备编号
    public static final String ACTION_SCREEN_ON = "com.cssweb.cloudag.ACTION_SCREEN_ON";
    public static final String ACTION_SCREEN_OFF = "com.cssweb.cloudag.ACTION_SCREEN_OFF";

    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PCA/upgrade/";
    public static final String PACKAGE_NAME = "com.cssweb.cloudag";
    public static final String PACKAGE_CLASS_NAME = "com.cssweb.cloudag.ui.activity.UserScreenActivity";
    public static String KEY_BLE_SCAN_RESULT = "key_ble_scan_result";


    public static class StationModeStatus {
        public static final int NORMAL = 0;
        public static final int CLOSED = 1;
        public static final int OUT_OF_SERVICE = 2;
        public static final int TRAIN_FAULT = 3;
        public static final int MAINTENANCE = 4;
        public static final int EMERGENCY = 5;
    }

    public static class EntryExitMode {
        public static final int NORMAL = 0;
        public static final int DENIED = 1;
        public static final int FREE = 2;
    }

    public static class TxnResultCode {
        public static final int OK = 0;

        public static final int RECYCLED_CARD = 12;
        public static final int MORE_THAN_ONE_CARD = 41;

        public static final int CARD_BREAK_POINT_RECOVERY = 23;
        public static final int READ_ERROR = 24;
        public static final int BLOCKED_CARD = 7;
        public static final int INSUFFICIENT_VALUE = 3;
        public static final int INSUFFICIENT_RIDES = 4;
        public static final int EXPIRED_CARD = 1;
        public static final int JOURNEY_STATUS_ERROR = 9;
        public static final int INVALID_ENTRY_STATION = 10;
        public static final int JOURNEY_OVER_TIME = 5;
        public static final int JOURNEY_OVER_RIDE = 32;
        public static final int JOURNEY_OVER_TIME_RIDE = 33;
        public static final int UNSUPPORTED_CARD = 6;
        //        public static final int CARD_IN_BLACK_LIST = 5;
        //        public static final int INVALID_SECTION = 11;
        //        public static final int INVALID_SECTION = 11;
        public static final int UNKNOW_ERROR = 11;
    }

    public static class TxnDetailCode {
        public static final String EXIT_NOT_SUPPORT_SJT = "243";
    }

    public static class CardPhyType {
        public static final int ULTRA_LIGHT = 1;
        public static final int MIFARE_ONE = 2;
        public static final int CPU = 3;
    }

    public static class TxnType {
        public static final int ENTRY = 1;
        public static final int EXIT = 2;
        public static final int ENTRY_ERROR_RESET = 11;
        public static final int EXIT_ERROR_RESET = 12;
    }

    public static class ProductCatgeory {
        /**
         * 钱包
         */
        public static final int PURSE = 1;
        /**
         * 计次
         */
        public static final int MULTI_RIDES = 2;
        /**
         * 定期
         */
        public static final int PERIOD = 3;
        /**
         * 用于员工卡等
         */
        public static final int AUTHORIZED_ID = 4;
        /**
         * 未定义
         */
        public static final int UNDEFINED = 255;
    }

    public static class Cmd {
        public static final int MODE_SETTING = 0x091210;
        public static final int TICKET_TRANSACTION = 0x091211;
        public static final int GATE_STATUS = 0x091213;
        public static final int INIT_CMD = 0x021003;
        public static final int APPUI_MAINTAIN_NOTIFY = 0x091300;
        public static final int MSG_SERVICE_MODE_SET = 0x093001;//运营模式设置
        public static final int DOOR_DIRECTION_TEST = 0x093011;//通行指示器设置
        public static final int GATE_OPEN_TEST = 0x093012;//门测试
        public static final int GATE_SENSOR_TEST = 0x093013;//传感器测试
        public static final int GATE_SENSOR_TEST_STATUS = 0x093014;//传感器Status
        public static final int MSG_FUNC_SWITCH = 0x093101;//维修功能切换
        public static final int MSG_QRSJT_IN_PROCESS_NOTIFY = 0x093201;//二维码处理通知
        public static final int MSG_QRSJT_BROADCAST_NOTIFY = 0x093202;//二维码广播通知
        //--------------------------------------------
        public static final int RESTART = 100090;
        public static final int HEART_BEAT = 88888;//used by local
        //        public static final int CONNECT_FAILED = -999999;//used by local
        public static final int SEND_HEART_BEAT_FAILED = -777777;//used by local
        public static final int SEND_HEART_BEAT_SUCCESS = -66666;//used by local
    }

    public static class IPARAM0 {
        public static final int MSG_GATE_SENSOR_TEST = 0x093013;//传感器检测
        public static final int POWER_OFF = 9;//关机
    }

    public static class LocalDeviceType {
        public static final int ENTRY_HOST = 11;
        public static final int ENTRY_SLAVE = 12;
        public static final int EXIT_HOST = 21;
        public static final int EXIT_SLAVE = 22;
    }

    public static class TicketRecycledStatus {
        public static final int All = 0;
        public static final int RECYCLE_ONLY = 1;
        public static final int SWIPE_ONLY = 2;
        public static final int OTHER = 3;

    }

    public static class GateStatus {
        public static final int NORMAL = 0;
        public static final int BREAKIN = 1;
        public static final int ERROR = 2;

    }

    public static class TicketType {
        public static final int SJT_ADULT = 256;//0x0100;

        public static final int MTR_ADULT = 512;//0x0200;
        public static final int MTR_STUDENT = 513;// 0x0201;
        public static final int MTR_FREE = 514;// 0x0202;
        public static final int MTR_DISCOUNT = 515;// 0x0203;
        public static final int MTR_MOBILE = 516;//0x0204;

        public static final int DAILY_TICKET2 = 802;//0x0322;

        public static final int ONE_DAY_TICKET = 801;//0x0321;
        public static final int DAILY_TICKET = 800;//0x0320;

        public static final int EP = 1792;// 0x0700;

        public static final int OCT_NORMAL = 2307;// 0x0903;
        public static final int OCT_MONTHLY = 2310;// 0x0906;
        public static final int OCT_MEMORIAL = 2323;// 0x0913;
        public static final int OCT_FREE = 2369;// 0x0941;
        public static final int OCT_DISCOUNT = 2370;// 0x0942;
        public static final int OCT_STUDENT = 2390;// 0x0956;
        public static final int OCT_PERSON_TICKET = 2417;// 0x0971;
        public static final int OCT_PERSON_SPARA = 2418;//0x0972;
        public static final int OCT_PERSON_PARA = 2419;// 0x0973;

        public static final int MTR_INTERVAL_TICKET = 802;// 0x0322;

        public static final int FINANCE_IC_CARD = 4097;// 0x1001;
        public static final int CLOUD_CARD = 4353;// 0x1101;


        public static final int PANCHAN_CARD = 39321;//9999
        public static final int APPLE_PAY = 34952;//8888

        public static final int SHENZHEN_A = 26214;//6666
        public static final int SHENZHEN_C = 30583;//7777

        public static final int QRCODE_TICKET = 257;//0X0101
        //---------shenzhen start
        /**
         * 地铁周票
         */
        public static final int SZ_MTR_WEEK = 80;
        /**
         * 地铁次票
         */
        public static final int SZ_MTR_TIMES1 = 81;
        /**
         * 一日票
         */
        public static final int SZ_MTR_ONEDAY = 82;
        /**
         * 深圳地铁计次票
         */
        public static final int SZ_MTR_TIMES2 = 89;
        /**
         * 员工票
         */
        public static final int SZ_SZT_STAFF = 88;
        /**
         * 非员工工作证
         */
        public static final int SZ_SZT_NSTAFF = 90;
        /**
         * 深圳通学生票
         */
        public static final int SZ_SZT_STUDENT1 = 5;
        /**
         * 深圳通学生票
         */
        public static final int SZ_SZT_STUDENT2 = 15;
        /**
         * 深圳通学生票
         */
        public static final int SZ_SZT_STUDENT3 = 25;
        /**
         * 手机深圳通
         */
        public static final int SZ_SZT_2DOT4G1 = 28;
        /**
         * 手机深圳通
         */
        public static final int SZ_SZT_2DOT4G2 = 46;
        /**
         * 纪念储值卡
         */
        public static final int SZ_SZT_MEMORY1 = 29;
        /**
         * 纪念储值卡
         */
        public static final int SZ_SZT_MEMORY2 = 66;
        /**
         * 残疾人卡
         */
        public static final int SZ_SZT_DISABLE = 21;
        /**
         * 单程票
         */
        public static final int SZ_SZT_SINGLE = 98;
        /**
         * 机场专用票
         */
        public static final int SZ_SZT_AIRPORT = 99;
        //深圳通-普通储值卡----START---
        /**
         * 深圳通-普通储值卡
         */
        public static final int SZ_SZT_STORE_CARD1 = 8;
        public static final int SZ_SZT_STORE_CARD2 = 18;
        public static final int SZ_SZT_STORE_CARD3 = 32;
        public static final int SZ_SZT_STORE_CARD4 = 33;
        public static final int SZ_SZT_STORE_CARD5 = 34;
        public static final int SZ_SZT_STORE_CARD6 = 35;
        public static final int SZ_SZT_STORE_CARD7 = 36;
        public static final int SZ_SZT_STORE_CARD8 = 44;
        public static final int SZ_SZT_STORE_CARD9 = 68;
        public static final int SZ_SZT_STORE_CARD10 = 69;
        public static final int SZ_SZT_STORE_CARD11 = 70;
        public static final int SZ_SZT_STORE_CARD12 = 2;
        public static final int SZ_SZT_STORE_CARD13 = 3;
        //深圳通-普通储值卡----END---

        //---------shenzhen end
    }

    public static class ServiceMode {
        public static final int NORMAL_SERVICE = 0;
        public static final int OUT_OF_SERVICE = 2;

    }

}

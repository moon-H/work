package socket.pad.common;

/**
 * Created by liwx on 2016/3/16.
 */
public class Common {
    public static class StationModeStatus {
        public static final int NORMAL = 0;
        public static final int CLOSED = 1;
        public static final int OUT_OF_SERVICE = 2;
        public static final int TRAIN_FAULT = 3;
        public static final int MAIN_TAINANCE = 4;
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
        public static final int UNKNOW_ERROR = 11;
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
        public static final int PURSE = 1;
        public static final int MULTI_RIDES = 2;
        public static final int PERIOD = 3;
        public static final int AUTHORIZEDID = 4;
    }

    public static class Cmd {
        public static final int MODE_SETTING = 0x091210;
        public static final int TICKET_TRANSACTION = 0x091211;
        public static final int GATE_STATUS = 0x091213;
        public static final int INIT_CMD = 0x021003;
        public static final int RESTART = 100090;
        public static final int HEART_BEAT = 88888;//used by local
        //        public static final int CONNECT_FAILED = -999999;//used by local
        public static final int SEND_HEART_BEAT_FAILED = -777777;//used by local
        public static final int SEND_HEART_BEAT_SUCCESS = -66666;//used by local
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
        public static final short SJT_ADULT = 0x0100;

        public static final short MTR_ADULT = 0x0200;
        public static final short MTR_STUDENT = 0x0201;
        public static final short MTR_FREE = 0x0202;
        public static final short MTR_DISCOUNT = 0x0203;
        public static final short MTR_MOBILE = 0x0204;

        public static final short DAILY_TICKET = 0x0320;

        public static final short EP = 0x0700;

        public static final short OCT_NORMAL = 0x0903;
        public static final short OCT_MONTHLY = 0x0906;
        public static final short OCT_MEMORIAL = 0x0913;
        public static final short OCT_FREE = 0x0941;
        public static final short OCT_DISCOUNT = 0x0942;
        public static final short OCT_STUDENT = 0x0956;
        public static final short OCT_PERSION_TICKET = 0x0971;
        public static final short OCT_PERSION_SPARA = 0x0972;
        public static final short OCT_PERSION_PARA = 0x0973;
        public static final int PANCHAN = 0x9999;

    }
}

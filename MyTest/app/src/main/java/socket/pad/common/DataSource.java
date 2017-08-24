package socket.pad.common;

/**
 * Created by lenovo on 2016/3/28.
 */
public class DataSource {
    /**
     * 进站请刷卡
     **/
    public static String ModeSetting_jinzhan_qingshuaka() {
        //        String str = "{CityCode=99, StationCode='01', DeviceId='11', StationModeId=2, StationModeStatus=2, EntryModeId=0, ExitModeId=1, TicketRecycledStatus=3, DeviceStatusCodeList=[11,22], VersionList=[1.0,2.0], LocalDeviceType=11}";
        //        Gson gson = new Gson();
        //        JsonElement resElement = gson.fromJson(str, JsonElement.class);
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.ENTRY_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setEntryModeId(Common.EntryExitMode.NORMAL);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.OTHER);
        ms.setExitModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();
    }

    /**
     * 紧急模式
     **/
    public static String ModeSetting_jinji() {
        //        String str = "{CityCode=99, StationCode='01', DeviceId='11', StationModeId=2, StationModeStatus=2, EntryModeId=0, ExitModeId=1, TicketRecycledStatus=3, DeviceStatusCodeList=[11,22], VersionList=[1.0,2.0], LocalDeviceType=11}";
        //        Gson gson = new Gson();
        //        JsonElement resElement = gson.fromJson(str, JsonElement.class);
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.ENTRY_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.MAIN_TAINANCE);
        ms.setEntryModeId(Common.EntryExitMode.NORMAL);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.OTHER);
        ms.setExitModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();
    }

    /**
     * 维修
     **/
    public static String ModeSetting_weixiu() {
        //        String str = "{CityCode=99, StationCode='01', DeviceId='11', StationModeId=2, StationModeStatus=2, EntryModeId=0, ExitModeId=1, TicketRecycledStatus=3, DeviceStatusCodeList=[11,22], VersionList=[1.0,2.0], LocalDeviceType=11}";
        //        Gson gson = new Gson();
        //        JsonElement resElement = gson.fromJson(str, JsonElement.class);
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.ENTRY_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.MAIN_TAINANCE);
        ms.setEntryModeId(Common.EntryExitMode.NORMAL);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.OTHER);
        ms.setExitModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();
    }
    /**
     * 禁止进站
     **/
    public static String ModeSetting_jinzhi_jinzhan() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.ENTRY_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setEntryModeId(Common.EntryExitMode.DENIED);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.OTHER);
        ms.setExitModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 禁止出站
     **/
    public static String ModeSetting_jinzhi_chuzhan() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setExitModeId(Common.EntryExitMode.DENIED);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.OTHER);
        ms.setEntryModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 请刷卡ALL
     **/
    public static String ModeSetting_qingshuaka_all() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setExitModeId(Common.EntryExitMode.NORMAL);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.All);
        ms.setEntryModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 出站只刷卡
     **/
    public static String ModeSetting_chuzhan_zhishuaka() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setExitModeId(Common.EntryExitMode.NORMAL);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.RECYCLE_ONLY);
        ms.setEntryModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 免检
     **/
    public static String ModeSetting_mianjian() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.NORMAL);
        ms.setExitModeId(Common.EntryExitMode.FREE);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.RECYCLE_ONLY);
        ms.setEntryModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 暂停服务
     **/
    public static String ModeSetting_zanting_fuwu() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setStationModeId(2);
        ms.setDeviceId("11");
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.OUT_OF_SERVICE);
        ms.setExitModeId(Common.EntryExitMode.FREE);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.RECYCLE_ONLY);
        ms.setEntryModeId(1);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();

    }

    /**
     * 紧急模式,禁止进站
     **/
    public static String ModeSetting_jinji_jinzhi_jinzhan() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.ENTRY_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.EMERGENCY);
        ms.setExitModeId(Common.EntryExitMode.FREE);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.RECYCLE_ONLY);
        ms.setEntryModeId(Common.EntryExitMode.DENIED);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();
    }

    /**
     * 紧急模式,立即出站
     **/
    public static String ModeSetting_jinji_lijichuzhan() {
        ModeSettings ms = new ModeSettings();
        ms.setCityCode(99);
        ms.setStationCode("01");
        ms.setDeviceId("11");
        ms.setStationModeId(2);
        String[] arr = {"11,22"};
        String[] arr2 = {"1.0,2.0"};
        ms.setDeviceStatusCodeList(arr);

        ms.setLocalDeviceType(Common.LocalDeviceType.EXIT_HOST);
        ms.setStationModeStatus(Common.StationModeStatus.EMERGENCY);
        ms.setExitModeId(Common.EntryExitMode.FREE);
        ms.setTicketRecycledStatus(Common.TicketRecycledStatus.RECYCLE_ONLY);
        ms.setEntryModeId(Common.EntryExitMode.DENIED);

        ms.setVersionList(arr2);

        System.out.println("#### :: " + ms.toString());
        return ms.toString();
    }

    /**
     * 单程票，普通成人，进站
     **/
    public static String Tran_ENTRY() {
        TicketTransaction ticketTransaction = new TicketTransaction();
        ticketTransaction.setTxnType(Common.TxnType.ENTRY);
        ticketTransaction.setTxnResultCode(Common.TxnResultCode.INSUFFICIENT_VALUE);
        ticketTransaction.setProductCategory(Common.ProductCatgeory.PURSE);
        ticketTransaction.setProductTypeId((short) Common.TicketType.PANCHAN);
        ticketTransaction.setProductRemainmingValue(1000);
        ticketTransaction.setTxnDetailCode("778899");
        ticketTransaction.setTransactionValue(50);
        ticketTransaction.setTicketUsedCount(2);
        System.out.println("#### :: " + ticketTransaction.toString());
        return ticketTransaction.toString();
    }

    /**
     * 单程票，普通成人，出站
     **/
    public static String Tran_EXIT() {
        TicketTransaction ticketTransaction = new TicketTransaction();
        ticketTransaction.setTxnType(Common.TxnType.EXIT);
        ticketTransaction.setTxnDetailCode("778899");
        ticketTransaction.setTxnResultCode(Common.TxnResultCode.EXPIRED_CARD);
        ticketTransaction.setProductCategory(Common.ProductCatgeory.PURSE);
        ticketTransaction.setProductTypeId(Common.TicketType.SJT_ADULT);
        ticketTransaction.setProductRemainmingValue(1000);
        ticketTransaction.setTransactionValue(50);
        System.out.println("#### :: " + ticketTransaction.toString());
        return ticketTransaction.toString();
    }
    /**
     * 单程票，普通成人，出站
     **/
    public static String Tran_EXIT2() {
        TicketTransaction ticketTransaction = new TicketTransaction();
        ticketTransaction.setTxnType(Common.TxnType.EXIT);
        ticketTransaction.setTxnDetailCode("778899");
        ticketTransaction.setTxnResultCode(Common.TxnResultCode.OK);
        ticketTransaction.setProductCategory(Common.ProductCatgeory.PURSE);
        ticketTransaction.setProductTypeId(Common.TicketType.OCT_NORMAL);
        ticketTransaction.setProductRemainmingValue(1000);
        ticketTransaction.setTransactionValue(50);
        ticketTransaction.setTicketUsedCount(2);
        System.out.println("#### :: " + ticketTransaction.toString());
        return ticketTransaction.toString();
    }
    /**
     * gate status
     **/
    public static String Gate_Status() {
        GateStatus status = new GateStatus();
        status.setGateExitStatus(Common.GateStatus.NORMAL);
        status.setGateExitSignalCount(0);
        status.setGateEntrySignalCount(0);
        status.setGateEntryStatus(Common.GateStatus.BREAKIN);
        System.out.println("#### :: " + status.toString());
        return status.toString();
    }
}

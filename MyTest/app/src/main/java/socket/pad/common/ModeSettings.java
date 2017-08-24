package socket.pad.common;

import java.util.Arrays;

/**
 * Created by liwx on 2016/3/16.
 */
public class ModeSettings extends Response {
    private int CityCode;
    private String StationCode;
    private String DeviceId;
    private int StationModeId;
    private int StationModeStatus;
    private int EntryModeId;
    private int ExitModeId;
    private int TicketRecycledStatus;
    private String[] DeviceStatusCodeList;
    private String[] VersionList;
    private int LocalDeviceType;

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getStationModeId() {
        return StationModeId;
    }

    public void setStationModeId(int stationModeId) {
        StationModeId = stationModeId;
    }

    public int getStationModeStatus() {
        return StationModeStatus;
    }

    public void setStationModeStatus(int stationModeStatus) {
        StationModeStatus = stationModeStatus;
    }

    public int getEntryModeId() {
        return EntryModeId;
    }

    public void setEntryModeId(int entryModeId) {
        EntryModeId = entryModeId;
    }

    public int getExitModeId() {
        return ExitModeId;
    }

    public void setExitModeId(int exitModeId) {
        ExitModeId = exitModeId;
    }

    public int getTicketRecycledStatus() {
        return TicketRecycledStatus;
    }

    public void setTicketRecycledStatus(int ticketRecycledStatus) {
        TicketRecycledStatus = ticketRecycledStatus;
    }

    public String[] getDeviceStatusCodeList() {
        return DeviceStatusCodeList;
    }

    public void setDeviceStatusCodeList(String[] deviceStatusCodeList) {
        DeviceStatusCodeList = deviceStatusCodeList;
    }

    public String[] getVersionList() {
        return VersionList;
    }

    public void setVersionList(String[] versionList) {
        VersionList = versionList;
    }

    public int getLocalDeviceType() {
        return LocalDeviceType;
    }

    public void setLocalDeviceType(int localDeviceType) {
        LocalDeviceType = localDeviceType;
    }

    @Override
    public String toString() {
        return "{" +
            "CityCode=" + CityCode +
            ", StationCode='" + StationCode + '\'' +
            ", DeviceId='" + DeviceId + '\'' +
            ", StationModeId=" + StationModeId +
            ", StationModeStatus=" + StationModeStatus +
            ", EntryModeId=" + EntryModeId +
            ", ExitModeId=" + ExitModeId +
            ", TicketRecycledStatus=" + TicketRecycledStatus +
            ", DeviceStatusCodeList=" + Arrays.toString(DeviceStatusCodeList) +
            ", VersionList=" + Arrays.toString(VersionList) +
            ", LocalDeviceType=" + LocalDeviceType +
            '}';
    }
}

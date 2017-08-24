package socket.pad.common;

/**
 * Created by liwx on 2016/3/16.
 */
public class GateStatus extends Response {
    private int GateEntryStatus;
    private int GateExitStatus;
    private int GateEntrySignalCount;
    private int GateExitSignalCount;

    public int getGateEntryStatus() {
        return GateEntryStatus;
    }

    public void setGateEntryStatus(int gateEntryStatus) {
        GateEntryStatus = gateEntryStatus;
    }

    public int getGateExitStatus() {
        return GateExitStatus;
    }

    public void setGateExitStatus(int gateExitStatus) {
        GateExitStatus = gateExitStatus;
    }

    public int getGateEntrySignalCount() {
        return GateEntrySignalCount;
    }

    public void setGateEntrySignalCount(int gateEntrySignalCount) {
        GateEntrySignalCount = gateEntrySignalCount;
    }

    public int getGateExitSignalCount() {
        return GateExitSignalCount;
    }

    public void setGateExitSignalCount(int gateExitSignalCount) {
        GateExitSignalCount = gateExitSignalCount;
    }

    @Override
    public String toString() {
        return "{" +
            "GateEntryStatus=" + GateEntryStatus +
            ", GateExitStatus=" + GateExitStatus +
            ", GateEntrySignalCount=" + GateEntrySignalCount +
            ", GateExitSignalCount=" + GateExitSignalCount +
            '}';
    }
}

package cn.unicompay.wallet.client.framework.model;

public class MySpService {
	private SpService spService;
	private int statusFlag;

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public SpService getSpService() {
		return spService;
	}

	public void setSpService(SpService spService) {
		this.spService = spService;
	}

	@Override
	public String toString() {
		return "MySpService [spService=" + spService + ", statusFlag="
				+ statusFlag + "]";
	}

}

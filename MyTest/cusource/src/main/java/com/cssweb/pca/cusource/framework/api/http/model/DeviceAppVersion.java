package cn.unicompay.wallet.client.framework.api.http.model;


public class DeviceAppVersion {
	private String appVersionName;
	private int deviceApplicationVersionIdx;
	private String appversionDesc;
	private boolean upgradeMandatory;
	private String versionRegDate;

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public int getDeviceApplicationVersionIdx() {
		return deviceApplicationVersionIdx;
	}

	public void setDeviceApplicationVersionIdx(int deviceApplicationVersionIdx) {
		this.deviceApplicationVersionIdx = deviceApplicationVersionIdx;
	}

	public String getAppversionDesc() {
		return appversionDesc;
	}

	public void setAppversionDesc(String appversionDesc) {
		this.appversionDesc = appversionDesc;
	}

	public boolean isUpgradeMandatory() {
		return upgradeMandatory;
	}

	public void setUpgradeMandatory(boolean upgradeMandatory) {
		this.upgradeMandatory = upgradeMandatory;
	}

	public String getVersionRegDate() {
		return versionRegDate;
	}

	public void setVersionRegDate(String versionRegDate) {
		this.versionRegDate = versionRegDate;
	}

}

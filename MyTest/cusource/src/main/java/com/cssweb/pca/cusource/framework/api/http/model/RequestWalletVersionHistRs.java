package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.List;

public class RequestWalletVersionHistRs extends ResultRs {
	PageInfo pageInfo;
	List<DeviceAppVersion> deviceAppVersionList;

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<DeviceAppVersion> getDeviceAppVersionList() {
		return deviceAppVersionList;
	}

	public void setDeviceAppVersionList(
			List<DeviceAppVersion> deviceAppVersionList) {
		this.deviceAppVersionList = deviceAppVersionList;
	}

}

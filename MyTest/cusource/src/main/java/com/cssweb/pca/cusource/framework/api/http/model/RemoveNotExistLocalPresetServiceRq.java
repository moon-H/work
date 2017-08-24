package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.List;

public class RemoveNotExistLocalPresetServiceRq {
	private String woAccountId;
	private List<String> serviceIdList;

	public String getWoAccountId() {
		return woAccountId;
	}

	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}

	public List<String> getServiceIdList() {
		return serviceIdList;
	}

	public void setServiceIdList(List<String> serviceIdList) {
		this.serviceIdList = serviceIdList;
	}

}

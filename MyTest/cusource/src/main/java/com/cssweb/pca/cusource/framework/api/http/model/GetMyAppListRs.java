package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.SpService;

public class GetMyAppListRs extends ResultRs{
	/**
	 * 
	 */
	private Vector<SpService> serviceList = null;

	/**
	 * @return
	 */
	public Vector<SpService> getServiceList() {
		return serviceList;
	}

	/**
	 * @param serviceList
	 */
	public void setServiceList(Vector<SpService> serviceList) {
		this.serviceList = serviceList;
	}
}

package cn.unicompay.wallet.client.framework.api.http.model;

import cn.unicompay.wallet.client.framework.model.SpService;

public class ViewAppInfoRs extends ResultRs{
	/**
	 * 
	 */
	private SpService service = null;

	/**
	 * @return
	 */
	public SpService getService() {
		return service;
	}

	/**
	 * @param service
	 */
	public void setService(SpService service) {
		this.service = service;
	}
}

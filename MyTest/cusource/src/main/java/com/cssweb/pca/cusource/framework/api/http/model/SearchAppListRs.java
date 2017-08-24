package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.SpService;

public class SearchAppListRs extends ResultRs{

	/**
	 * 
	 */
	private PageInfo pageInfo = null;
	/**
	 * 
	 */
	private Vector<SpService> serviceList = null;
	
	/**
	 * @return
	 */
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	
	/**
	 * @param pageInfo
	 */
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

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

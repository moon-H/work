package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.SpService;

/**
 * @author hj21
 *
 */
public class GetGuiAppListRs extends ResultRs{
	/**
	 * 
	 */
	private Vector<SpService> appList = null;
	
	/**
	 * @return
	 */
	public Vector<SpService> getAppList() {
		return appList;
	}
	
	/**
	 * @param appList
	 */
	public void setAppList(Vector<SpService> appList) {
		this.appList = appList;
	}
}

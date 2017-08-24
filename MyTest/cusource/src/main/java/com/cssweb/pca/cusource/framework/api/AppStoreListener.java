package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.SpService;

public interface AppStoreListener extends NetworkListener {
	/**
	 * available SP application list
	 * @param apps
	 */
	public void onList(Vector<SpService> apps);
	
	/**
	 * 
	 */
	public void onError(int errorCode,String errorMsg);
	
	/**
	 * SP application's detail information
	 * @param app
	 */
	public void onDetail(SpService app);
}

package cn.unicompay.wallet.client.framework.api.http.model;

public class BaseAppInfo {
	/**
	 * 
	 */
	private String appId = null;
	/**
	 * 
	 */
	private String appVersion = null;
	
	/**
	 * @return
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * @return
	 */
	public String getAppVersion() {
		return appVersion;
	}
	
	/**
	 * @param appVersion
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
}

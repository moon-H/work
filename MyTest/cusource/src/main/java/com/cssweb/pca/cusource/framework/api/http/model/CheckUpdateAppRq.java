package cn.unicompay.wallet.client.framework.api.http.model;

/**
 * @author hj21
 *
 */
public class CheckUpdateAppRq {
	/**
	 * 
	 */
	private String serviceId = null;
	/**
	 * 
	 */
	private String appVersion = null;
	
	/**
	 * @param serviceId
	 * @param appVersion
	 */
	public CheckUpdateAppRq(String serviceId, String appVersion) {
		this.serviceId = serviceId;
		this.appVersion = appVersion;
	}
	
	/**
	 * 
	 */
	public CheckUpdateAppRq() {}
	
	/**
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

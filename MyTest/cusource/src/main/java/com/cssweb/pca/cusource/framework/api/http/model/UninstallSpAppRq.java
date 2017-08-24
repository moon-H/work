package cn.unicompay.wallet.client.framework.api.http.model;

public class UninstallSpAppRq {
	/**
	 * 
	 */
	private String serviceId = null;
	
	/**
	 * @param serviceId
	 */
	public UninstallSpAppRq(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 */
	public UninstallSpAppRq() {}

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
}

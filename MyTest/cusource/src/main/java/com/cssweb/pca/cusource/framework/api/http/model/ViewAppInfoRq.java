package cn.unicompay.wallet.client.framework.api.http.model;

public class ViewAppInfoRq {
	/**
	 * 
	 */
	private String serviceId = null;

	/**
	 * 
	 */
	public ViewAppInfoRq() {}

	/**
	 * @param serviceId
	 */
	public ViewAppInfoRq(String serviceId) {
		this.serviceId = serviceId;
	}

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

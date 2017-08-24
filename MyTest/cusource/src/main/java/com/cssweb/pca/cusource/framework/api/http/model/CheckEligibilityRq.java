package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckEligibilityRq {
	/**
	 * 
	 */
	private String serviceId = null;

	/**
	 * @param serviceId
	 */
	public CheckEligibilityRq(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 */
	public CheckEligibilityRq() {}

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

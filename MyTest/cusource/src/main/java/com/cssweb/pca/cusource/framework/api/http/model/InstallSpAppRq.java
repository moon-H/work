package cn.unicompay.wallet.client.framework.api.http.model;

public class InstallSpAppRq {
	/**
	 * 
	 */
	private String serviceId = null;
	
	/**
	 * 
	 */
	private String serviceVersion = null;

	public InstallSpAppRq(String serviceId, String serviceVersion) {
		this.serviceId = serviceId;
		this.serviceVersion = serviceVersion;
	}
	
	public InstallSpAppRq(){}

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
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * @param serviceVersion
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
}

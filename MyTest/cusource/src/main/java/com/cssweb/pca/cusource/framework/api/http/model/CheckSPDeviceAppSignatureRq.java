package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckSPDeviceAppSignatureRq {
	private String serviceId;
	private String signature;
	private String mobileUniqueId;
	private String mobileUidType;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getMobileUniqueId() {
		return mobileUniqueId;
	}
	public void setMobileUniqueId(String mobileUniqueId) {
		this.mobileUniqueId = mobileUniqueId;
	}
	public String getMobileUidType() {
		return mobileUidType;
	}
	public void setMobileUidType(String mobileUidType) {
		this.mobileUidType = mobileUidType;
	}


}

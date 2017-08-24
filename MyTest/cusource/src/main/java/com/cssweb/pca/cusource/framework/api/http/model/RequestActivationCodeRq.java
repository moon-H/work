package cn.unicompay.wallet.client.framework.api.http.model;

public class RequestActivationCodeRq {
	
	private String msisdn;  
	private String mobileUniqueId;
	private String mobileUidType;
	
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

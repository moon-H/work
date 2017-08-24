package cn.unicompay.wallet.client.framework.api.http.model;

public class RequestCustomerIdbyMobileUIDRq {
	private String mobileUniqueId;
	private String mobileUidType;
	
	public RequestCustomerIdbyMobileUIDRq(String mobileUniqueId,String mobileUidType){
		this.mobileUniqueId = mobileUniqueId;
		this.mobileUidType = mobileUidType;
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

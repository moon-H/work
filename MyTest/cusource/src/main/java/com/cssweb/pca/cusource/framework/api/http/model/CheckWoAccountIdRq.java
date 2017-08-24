package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckWoAccountIdRq {

	private String mobileUniqueId;
	private String mobileUidType;
	private String woAccountId;
	
	public String getWoAccountId() {
		return woAccountId;
	}
	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
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

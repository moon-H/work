package cn.unicompay.wallet.client.framework.api.http.model;

public class MobileStatusRq
{
	private String userId = null;
	
	private String mobileUniqueId;
	private String mobileUidType;
	private String woAccountId;
	
	
	
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

	public String getWoAccountId() {
		return woAccountId;
	}

	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}

	private ClientInfo clientInfo = null;
	

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}


	/**
	 * @param mobileId
	 * @param language
	 */
	

	/**
	 * 
	 */
	public MobileStatusRq() {}

	/**
	 * @return
	 */
	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	/**
	 * @param clientInfo
	 */
	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
}

package cn.unicompay.wallet.client.framework.api.http.model;

public class LoginWalletClientRq {
	
	private String woAccountId = null;
	
	private String woAccountPwd = null;
	
	private ClientInfo clientInfo = null;
	
	private String mobileUniqueId;
	private String mobileUidType;
	
	

	

	public String getWoAccountId() {
		return woAccountId;
	}

	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}

	public String getWoAccountPwd() {
		return woAccountPwd;
	}

	public void setWoAccountPwd(String woAccountPwd) {
		this.woAccountPwd = woAccountPwd;
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

	/**
	 * @param mobileId
	 * @param language
	

	/**
	 * 
	 */
	public LoginWalletClientRq() {}

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

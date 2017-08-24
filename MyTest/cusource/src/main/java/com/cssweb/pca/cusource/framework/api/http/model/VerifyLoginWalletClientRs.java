package cn.unicompay.wallet.client.framework.api.http.model;

public class VerifyLoginWalletClientRs extends ResultRs{
	/**
	 * 
	 */
	private String hMAC = null;
	
	private String realName;
	private String userIdType;
	private String userId;
	
	
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserIdType() {
		return userIdType;
	}

	public void setUserIdType(String userIdType) {
		this.userIdType = userIdType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public String gethMAC() {
		return hMAC;
	}
	
	/**
	 * @param hMAC
	 */
	public void sethMAC(String hMAC) {
		this.hMAC = hMAC;
	}
}

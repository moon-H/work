package cn.unicompay.wallet.client.framework.api.http.model;

public class ActivateWoAccountRq {
	private String woAccountId;
	private String loginPwd;
	private String walletId;
	private String mobileUniqueId; // iccid
	private String mobileUidType;
	private String modelName;
	private String imei;
	private String imsi;
	private String customerInfoExistYN;
	private String payPwd;
	
	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getWoAccountId() {
		return woAccountId;
	}

	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getCustomerInfoExistYN() {
		return customerInfoExistYN;
	}

	public void setCustomerInfoExistYN(String customerInfoExistYN) {
		this.customerInfoExistYN = customerInfoExistYN;
	}

}

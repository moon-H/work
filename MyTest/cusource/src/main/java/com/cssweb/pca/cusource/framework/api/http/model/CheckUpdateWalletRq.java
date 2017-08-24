package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckUpdateWalletRq {
	/**
	 * 
	 */
	private String walletId = null;
	/**
	 * 
	 */
	private String appVersionName = null;
	
	/**
	 * 
	 */
	private String mobileUniqueId = null;
	private String mobileUidType = null;
	private String modelName = null;
	
	/**
	 * 
	 */
	private String imei = null;
	
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

	public CheckUpdateWalletRq(){}
	
	/**
	 * @param walletId
	 * @param appVersionName
	 * @param imei
	 */


	/**
	 * @return
	 */
	public String getWalletId() {
		return walletId;
	}

	/**
	 * @param walletId
	 */
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}


	/**
	 * @return
	 */
	public String getAppVersionName() {
		return appVersionName;
	}

	/**
	 * @param appVersionName
	 */
	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	/**
	 * @return
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
}

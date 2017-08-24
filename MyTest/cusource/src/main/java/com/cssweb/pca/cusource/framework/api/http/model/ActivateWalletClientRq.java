package cn.unicompay.wallet.client.framework.api.http.model;

public class ActivateWalletClientRq {
	
	private ClientInfo clientInfo =null;
	
	private BaseAppInfo baseAppInfo = null;
	
	private String walletId;
	private String mobileId;
	private String mobileUniqueId = null;	// iccid
	private String mobileUidType = null;
	private String modelName = null;
	private String imei = null;
	private String activationCode = null;
	
	
	public String getMobileUidType() {
		return mobileUidType;
	}

	public void setMobileUidType(String mobileUidType) {
		this.mobileUidType = mobileUidType;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

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
	
	/**
	 * @return
	 */
	public String getModelName() {
		return modelName;
	}
	
	/**
	 * @param modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public BaseAppInfo getBaseAppInfo()
	{
		return baseAppInfo;
	}

	public void setBaseAppInfo(BaseAppInfo baseAppInfo)
	{
		this.baseAppInfo = baseAppInfo;
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
	
	/**
	 * @return
	 */
	public String getActivationCode() {
		return activationCode;
	}
	
	/**
	 * @param activationCode
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * @return
	 */
	public String getMobileUniqueId() {
		return mobileUniqueId;
	}

	/**
	 * @param mobileUniqueId
	 */
	public void setMobileUniqueId(String mobileUniqueId) {
		this.mobileUniqueId = mobileUniqueId;
	}
	
	
}

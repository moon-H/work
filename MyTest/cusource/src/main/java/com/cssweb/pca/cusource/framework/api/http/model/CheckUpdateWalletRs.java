package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckUpdateWalletRs extends ResultRs{
	/**
	 * 
	 */
	private String updateYn = null;
	/**
	 * 
	 */
	private String walletId = null;
	/**
	 * 
	 */
	private String appName = null;
	/**
	 * 
	 */
	private String appVersionName = null;
	/**
	 * 
	 */
	private String appDownloadUrl = null;
	/**
	 * 
	 */
	private String updateMandatoryYn = null;
	
	/**
	 * @return
	 */
	private String appversionDesc=null;
	/**
	 * @return
	 */
	public String getUpdateYn() {
		return updateYn;
	}
	
	public String getAppversionDesc() {
		return appversionDesc;
	}

	public void setAppversionDesc(String appversionDesc) {
		this.appversionDesc = appversionDesc;
	}

	/**
	 * @param updateYn
	 */
	public void setUpdateYn(String updateYn) {
		this.updateYn = updateYn;
	}
	
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
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
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
	public String getAppDownloadUrl() {
		return appDownloadUrl;
	}

	/**
	 * @param appDownloadUrl
	 */
	public void setAppDownloadUrl(String appDownloadUrl) {
		this.appDownloadUrl = appDownloadUrl;
	}
	
	/**
	 * @return
	 */
	public String getUpdateMandatoryYn() {
		return updateMandatoryYn;
	}
	
	/**
	 * @param updateMandatoryYn
	 */
	public void setUpdateMandatoryYn(String updateMandatoryYn) {
		this.updateMandatoryYn = updateMandatoryYn;
	}
	
}

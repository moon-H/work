package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckUpdateAppRs extends ResultRs{
	/**
	 * 
	 */
	private String updateYn = null;
	/**
	 * 
	 */
	private String updateMandatoryYn = null;
	/**
	 * 
	 */
	private String appId = null;
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
	 * @return
	 */
	public String getUpdateYn() {
		return updateYn;
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
	public String getUpdateMandatoryYn() {
		return updateMandatoryYn;
	}

	/**
	 * @param updateMandatoryYn
	 */
	public void setUpdateMandatoryYn(String updateMandatoryYn) {
		this.updateMandatoryYn = updateMandatoryYn;
	}

	/**
	 * @return
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
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
}

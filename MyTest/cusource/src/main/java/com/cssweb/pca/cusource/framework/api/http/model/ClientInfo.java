package cn.unicompay.wallet.client.framework.api.http.model;

public class ClientInfo {
	
	/**
	 * 
	 */
	private String language = null;
	/**
	 * 
	 */
	private String mobileId = null;
	
	/**
	 * 
	 */
	public ClientInfo(){}
	
	/**
	 * @param mobileId
	 * @param language
	 */
	public ClientInfo(String mobileId, String language) {
		this.mobileId = mobileId;
		this.language = language;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return
	 */
	public String getMobileId() {
		return mobileId;
	}

	/**
	 * @param mobileId
	 */
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

}

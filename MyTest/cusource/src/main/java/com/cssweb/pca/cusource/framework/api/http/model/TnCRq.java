package cn.unicompay.wallet.client.framework.api.http.model;

public class TnCRq {
	private String walletId=null;
	/**
	 * 
	 */
	private String language = null;
	
	/**
	 * 
	 */
	public TnCRq(){}
	
	/**
	 * @param walletId
	 */
	public TnCRq(String walletId,String language){
		this.walletId = walletId;
		this.language = language;
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
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
}

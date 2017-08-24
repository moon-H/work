package cn.unicompay.wallet.client.framework.api.http.model;

public class MobileAuthRq {
	
	private ClientInfo clientInfo = null;
	
	/**
	 * @param mobileId
	 * @param language
	 */
	public MobileAuthRq(String mobileId, String language) {
		this.clientInfo = new ClientInfo(mobileId, language);
	}
	
	/**
	 * 
	 */
	public MobileAuthRq() {}
	
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

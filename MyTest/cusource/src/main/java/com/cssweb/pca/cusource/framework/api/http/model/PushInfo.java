package cn.unicompay.wallet.client.framework.api.http.model;

public class PushInfo {
	/**
	 * 
	 */
	private String pushType = null;
	/**
	 * 
	 */
	private String pushId = null;
	/**
	 * 
	 */
	public PushInfo(){}
	/**
	 * @param pushType
	 * @param pushId
	 */
	public PushInfo(String pushType,String pushId){}
	/**
	 * @return
	 */
	public String getPushType() {
		return pushType;
	}
	
	/**
	 * @param pushType
	 */
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	/**
	 * @return
	 */
	public String getPushId() {
		return pushId;
	}
	
	/**
	 * @param pushId
	 */
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	
}

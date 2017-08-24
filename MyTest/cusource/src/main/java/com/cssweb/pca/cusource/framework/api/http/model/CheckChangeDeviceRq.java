package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckChangeDeviceRq {
	/**
	 * 
	 */
	private String mobileUid = null;
	/**
	 * 
	 */
	private String imei = null;
	
	/**
	 * 
	 */
	public CheckChangeDeviceRq(){}
	
	/**
	 * @param mobileUid
	 * @param imei
	 */
	public CheckChangeDeviceRq(String mobileUid,String imei){
		this.mobileUid = mobileUid;
		this.imei = imei;
	}

	/**
	 * @return
	 */
	public String getMobileUid() {
		return mobileUid;
	}

	/**
	 * @param mobileUid
	 */
	public void setMobileUid(String mobileUid) {
		this.mobileUid = mobileUid;
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

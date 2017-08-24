package cn.unicompay.wallet.client.framework.api.http.model;

public class ConfirmNotiRq {
	/**
	 * 
	 */
	public static final int CONFORM_RECEIVED = 1;
	/**
	 * 
	 */
	public static final int CONFORM_FINISH = 2;
	
	/**
	 * 
	 */
	private String notiTid = null;
	/**
	 * 
	 */
	private Integer confirmType= null;
	
	/**
	 * @return
	 */
	public String getNotiTid() {
		return notiTid;
	}
	
	/**
	 * @param notiTid
	 */
	public void setNotiTid(String notiTid) {
		this.notiTid = notiTid;
	}

	/**
	 * @return
	 */
	public Integer getConfirmType() {
		return confirmType;
	}

	/**
	 * @param confirmType
	 */
	public void setConfirmType(Integer confirmType) {
		this.confirmType = confirmType;
	}
}

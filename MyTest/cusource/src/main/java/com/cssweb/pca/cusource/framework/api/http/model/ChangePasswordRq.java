package cn.unicompay.wallet.client.framework.api.http.model;

public class ChangePasswordRq {

	private String mobileId;
	private String woAccountId;
	private String oldPsw;
	private String newPsw;
	
	
	public String getMobileId() {
		return mobileId;
	}
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	public String getWoAccountId() {
		return woAccountId;
	}
	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}
	public String getOldPsw() {
		return oldPsw;
	}
	public void setOldPsw(String oldPsw) {
		this.oldPsw = oldPsw;
	}
	public String getNewPsw() {
		return newPsw;
	}
	public void setNewPsw(String newPsw) {
		this.newPsw = newPsw;
	}
	
	
	
}

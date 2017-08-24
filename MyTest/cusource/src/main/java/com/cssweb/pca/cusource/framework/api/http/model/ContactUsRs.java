package cn.unicompay.wallet.client.framework.api.http.model;

public class ContactUsRs extends ResultRs{
	/**
	 * 
	 */
	private String email = null;
	/**
	 * 
	 */
	private String phone = null;
	
	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
}

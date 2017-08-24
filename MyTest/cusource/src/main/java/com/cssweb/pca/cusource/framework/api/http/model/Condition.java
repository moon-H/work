package cn.unicompay.wallet.client.framework.api.http.model;

public class Condition {
	/**
	 * 
	 */
	public static final String TYPE_NAME = "serviceName";
	/**
	 * 
	 */
	public static final String TYPE_CATEGORY = "categoryId";
	/**
	 * 
	 */
	public static final String TYPE_PROVIDER_ID = "providerId";
	/**
	 * 
	 */
	public static final String TYPE_PROVIDER_NAME = "providerName";
	/**
	 * 
	 */
	public static final String TYPE_IS_POPULAR = "isPopular";
	/**
	 * 
	 */
	private String type = null;
	/**
	 * 
	 */
	private String value = null;
	
	/**
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}

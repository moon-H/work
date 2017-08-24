package cn.unicompay.wallet.client.framework.model;

public class Category {
	/** The ID **/
	private String categoryId = null;
	/** The name **/
	private String categoryName = null;
	
	/**
	 * @return
	 */
	public String getCategoryId() {
		return categoryId;
	}
	
	/**
	 * @param categoryId
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	/**
	 * @return
	 */
	public String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * @param categoryName
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}

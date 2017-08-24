package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Category;

public class ViewCategoryRs extends ResultRs{

	/**
	 * 
	 */
	private Vector<Category> categoryList = null;

	/**
	 * @return
	 */
	public Vector<Category> getCategoryList() {
		return categoryList;
	}

	/**
	 * @param categoryList
	 */
	public void setCategoryList(Vector<Category> categoryList) {
		this.categoryList = categoryList;
	}
}

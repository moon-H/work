package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

/**
 * @author hj21
 *
 */
public class SearchAppListRq {
	/**
	 * 
	 */
	private PageInfo pageInfo = null;
	/**
	 * 
	 */
	private Vector<Condition> conditionList = null;
	
	/**
	 * @return
	 */
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	
	/**
	 * @param pageInfo
	 */
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	/**
	 * @return
	 */
	public Vector<Condition> getConditionList() {
		return conditionList;
	}
	
	/**
	 * @param conditionList
	 */
	public void setConditionList(Vector<Condition> conditionList) {
		this.conditionList = conditionList;
	}
}

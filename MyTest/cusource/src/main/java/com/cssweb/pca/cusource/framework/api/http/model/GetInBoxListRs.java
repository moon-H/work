package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.InBoxMessage;

public class GetInBoxListRs extends ResultRs {
	/**
	 * 
	 */
	private PageInfo pageInfo;
	/**
	 * 
	 */
	private Vector<InBoxMessage> inboxMessageList;
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
	public Vector<InBoxMessage> getInboxMessageList() {
		return inboxMessageList;
	}

	/**
	 * @param inboxMessageList
	 */
	public void setInboxMessageList(Vector<InBoxMessage> inboxMessageList) {
		this.inboxMessageList = inboxMessageList;
	}
}

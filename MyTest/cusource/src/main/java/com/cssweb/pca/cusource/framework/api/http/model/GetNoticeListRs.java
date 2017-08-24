package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Notice;

public class GetNoticeListRs extends ResultRs{
	/**
	 * 
	 */
	private Vector<Notice> noticeList = null;
	/**
	 * 
	 */
	private PageInfo pageInfo =null;
	
	/**
	 * @return
	 */
	public Vector<Notice> getNoticeList() {
		return noticeList;
	}
	
	/**
	 * @param noticeList
	 */
	public void setNoticeList(Vector<Notice> noticeList) {
		this.noticeList = noticeList;
	}
	
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
}

package cn.unicompay.wallet.client.framework.api.http.model;

/**
 * @author SKCCADMIN
 *
 */
public class GetInBoxListRq {
	/**
	 * 
	 */
	private PageInfo pageInfo = null;

	/**
	 * @param pageNumber
	 * @param pageSize
	 */
	public GetInBoxListRq(int pageNumber,int pageSize) {
		this.pageInfo = new PageInfo(pageNumber, pageSize);
	}

	/**
	 * 
	 */
	public GetInBoxListRq() {}

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

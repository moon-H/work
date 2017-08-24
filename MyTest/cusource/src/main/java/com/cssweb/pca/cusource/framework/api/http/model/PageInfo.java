package cn.unicompay.wallet.client.framework.api.http.model;

public class PageInfo {
	/**
	 * 
	 */
	private Integer totalPage = null;
	/**
	 * 
	 */
	private Integer pageNumber = null;
	/**
	 * 
	 */
	private Integer pageSize = null;
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 */
	public PageInfo(int pageNumber, int pageSize) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	/**
	 * 
	 */
	public PageInfo(){}

	/**
	 * @return
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage
	 */
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

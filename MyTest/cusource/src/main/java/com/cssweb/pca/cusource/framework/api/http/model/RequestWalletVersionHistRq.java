package cn.unicompay.wallet.client.framework.api.http.model;

public class RequestWalletVersionHistRq {
	String walletId;
	PageInfo pageInfo;

	public RequestWalletVersionHistRq(int pageNumber, int pageSize) {
		this.pageInfo = new PageInfo(pageNumber, pageSize);
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
}

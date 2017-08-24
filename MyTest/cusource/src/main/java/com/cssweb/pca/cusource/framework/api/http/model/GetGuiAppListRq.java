package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

/**
 * @author hj21
 *
 */
public class GetGuiAppListRq {
	/**
	 * 
	 */
	private Vector<String> aidList = null;

	/**
	 * @param aidList
	 */
	public GetGuiAppListRq(Vector<String> aidList) {
		this.aidList = aidList;
	}

	/**
	 * 
	 */
	public GetGuiAppListRq() {}

	/**
	 * @return
	 */
	public Vector<String> getAidList() {
		return aidList;
	}

	/**
	 * @param aidList
	 */
	public void setAidList(Vector<String> aidList) {
		this.aidList = aidList;
	}
}

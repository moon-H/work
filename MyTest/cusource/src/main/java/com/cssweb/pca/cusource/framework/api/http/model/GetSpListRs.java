package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.SP;

public class GetSpListRs extends ResultRs{

	private Vector<SP> spList = null;

	/**
	 * @return
	 */
	public Vector<SP> getSpList() {
		return spList;
	}

	/**
	 * @param spList
	 */
	public void setSpList(Vector<SP> spList) {
		this.spList = spList;
	}
}

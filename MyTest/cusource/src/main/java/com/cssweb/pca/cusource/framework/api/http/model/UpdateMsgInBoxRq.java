package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.List;

import cn.unicompay.wallet.client.framework.model.InBoxMessage;

public class UpdateMsgInBoxRq {
	/**
	 * 
	 */
	// private String messageId;
	// private List<InBoxMessage> upDateInboxList;

	private List<InBoxMessage> updateInboxMsgStatusList;

	public List<InBoxMessage> getUpdateInboxMsgStatusList() {
		return updateInboxMsgStatusList;
	}

	public void setUpdateInboxMsgStatusList(
			List<InBoxMessage> updateInboxMsgStatusList) {
		this.updateInboxMsgStatusList = updateInboxMsgStatusList;
	}

}

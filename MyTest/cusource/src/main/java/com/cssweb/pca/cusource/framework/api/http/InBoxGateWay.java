package cn.unicompay.wallet.client.framework.api.http;

import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.http.model.DeleteInBoxRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetInBoxListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetInBoxListRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;
import cn.unicompay.wallet.client.framework.api.http.model.UpdateMsgInBoxRq;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.http.gateway.BaseGateWay;

public class InBoxGateWay extends BaseGateWay {

	public InBoxGateWay(NetworkManager networkMgr) {
		super(networkMgr);
	}

	/**
	 * Retrieve all messages in my box.
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetInBoxListRs getInboxMessageList(final GetInBoxListRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, GetInBoxListRs.class, session.getServerUrl()
				+ "/inbox/getInboxMessageList", "getInboxMessageListRq");
	}

	/**
	 * Delete a message in my box.
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs deleteInboxMessage(final DeleteInBoxRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/inbox/deleteInboxMessage", "deleteInboxMessageRq");
	}

	/**
	 * Set the status of message read in my box.
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs updateInboxMessageStatus(final UpdateMsgInBoxRq req) throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/inbox/updateInboxMessageStatus",
				"updateInboxMessageStatusRq");
	}
}

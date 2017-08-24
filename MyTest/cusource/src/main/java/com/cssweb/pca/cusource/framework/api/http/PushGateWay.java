package cn.unicompay.wallet.client.framework.api.http;

import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.http.model.ConfirmNotiRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetMyNotiRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.http.gateway.BaseGateWay;

public class PushGateWay extends BaseGateWay {
	/**
	 * @param networkMgr
	 */
	public PushGateWay(NetworkManager networkMgr) {
		super(networkMgr);
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetMyNotiRs getMyNoti() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(emptyRq, GetMyNotiRs.class, session.getServerUrl()
				+ "/push/getMyNoti", "getMyNotiRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs confirmNoti(final ConfirmNotiRq req) throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/push/confirmNoti", "confirmNotiRq");
	}
}

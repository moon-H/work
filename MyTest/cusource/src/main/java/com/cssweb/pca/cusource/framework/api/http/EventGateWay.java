package cn.unicompay.wallet.client.framework.api.http;

import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.http.model.GetAllEventListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetBrandListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetBrandListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetEventDetailRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetEventDetailRs;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.http.gateway.BaseGateWay;

public class EventGateWay extends BaseGateWay {

	public EventGateWay(NetworkManager networkMgr) {
		super(networkMgr);
	}

	public GetAllEventListRs getAllEventList() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(emptyRq, GetAllEventListRs.class,
				session.getServerUrl() + "/event/getAllEvent", "getAllEventRq");
	}

	public GetEventDetailRs getEventDetail(final GetEventDetailRq rq)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(rq, GetEventDetailRs.class, session.getServerUrl()
				+ "/event/getEventDetail", "getEventDetailRq");
	}

	public GetBrandListRs getBrandList(final GetBrandListRq rq)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(rq, GetBrandListRs.class, session.getServerUrl()
				+ "/event/getBrand", "getBrandRq");
	}

}

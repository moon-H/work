package cn.unicompay.wallet.client.framework.api.http;

import android.util.Log;
import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.http.model.CheckEligibilityRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateAppRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateAppRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetGuiAppListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetGuiAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetMyAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetSpListRs;
import cn.unicompay.wallet.client.framework.api.http.model.InstallSpAppRq;
import cn.unicompay.wallet.client.framework.api.http.model.RecordSpOrderRq;
import cn.unicompay.wallet.client.framework.api.http.model.RecordSpOrderRs;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveAuthFailedRq;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveAuthFailedRs;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveNotExistLocalPresetServiceRq;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveNotExistLocalPresetServiceRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;
import cn.unicompay.wallet.client.framework.api.http.model.SearchAppListRq;
import cn.unicompay.wallet.client.framework.api.http.model.SearchAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.UninstallSpAppRq;
import cn.unicompay.wallet.client.framework.api.http.model.ViewAppInfoRq;
import cn.unicompay.wallet.client.framework.api.http.model.ViewAppInfoRs;
import cn.unicompay.wallet.client.framework.api.http.model.ViewCategoryRs;

import com.skcc.wallet.core.http.HttpResult;
import com.skcc.wallet.core.http.HttpTask;
import com.skcc.wallet.core.http.WalletHttpConnection;
import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.http.gateway.BaseGateWay;
import com.skcc.wallet.core.util.NetworkUtil;

public class SpAppGateWay extends BaseGateWay {
	/**
	 * @param networkMgr
	 */
	public SpAppGateWay(NetworkManager networkMgr) {
		super(networkMgr);
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ViewCategoryRs viewCategory() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(emptyRq, ViewCategoryRs.class, session.getServerUrl()
				+ "/spservice/viewCategory", "viewCategoryRq");
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetSpListRs getSpList() throws NoNetworkException,
			NoResponseException, ResNotOKException {

		return transmit(emptyRq, GetSpListRs.class, session.getServerUrl()
				+ "/spservice/getSpList", "getSpListRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public SearchAppListRs searchServiceList(final SearchAppListRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, SearchAppListRs.class, session.getServerUrl()
				+ "/spservice/searchServiceList", "searchServiceListRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ViewAppInfoRs viewServiceInfo(final ViewAppInfoRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ViewAppInfoRs.class, session.getServerUrl()
				+ "/spservice/viewServiceInfo", "viewServiceInfoRq");
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetMyAppListRs getMyServiceList() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(emptyRq, GetMyAppListRs.class, session.getServerUrl()
				+ "/spservice/getMyServiceList", "getMyServiceListRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetGuiAppListRs getGuiServiceList(final GetGuiAppListRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, GetGuiAppListRs.class, session.getServerUrl()
				+ "/spservice/getGuiServiceList", "getGuiServiceListRq");

	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs installSpService(final InstallSpAppRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/spservice/installSpService", "installSpServiceRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs uninstallSpService(final UninstallSpAppRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/spservice/uninstallSpService", "uninstallSpServiceRq");

	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public CheckUpdateAppRs checkUpdateApp(final CheckUpdateAppRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, CheckUpdateAppRs.class, session.getServerUrl()
				+ "/spservice/checkUpdateApp", "checkUpdateAppRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs checkEligibility(final CheckEligibilityRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/spservice/checkEligibility", "checkEligibilityRq");
	}

	// TODO: 상세 구현할 것.
	public String transmit(String jsonData) throws NoNetworkException,
			NoResponseException {

		// return transmit(jsonData, String, session.getServerUrl()
		// + "/spservice/transmit", "transmitRq");
		Log.d("transmit", "transmitRq>>>>>>>>>>>>>>>>" + jsonData);
		// SpPluginRs str = transmit(jsonData, SpPluginRs.class,
		// session.getServerUrl() + "/spservice/transmit", "transmitRq");
		HttpTask httpTask = new HttpTask(session.getServerUrl()
				+ "/spservice/transmit", "transmitRq");
		httpTask.setReqBody(jsonData);
		HttpResult httpResult = transmitRequest(httpTask);

		if (httpResult.getHttpCode() == 200) {
			session.setCookie(httpResult.getCookie());
		}
		Log.d("transmit", "transmitRs>>>>>>>>>>>>>>>>" + httpResult.getRes());
		return httpResult.getRes();
	}

	private HttpResult transmitRequest(HttpTask httptask)
			throws NoNetworkException, NoResponseException {
		if (NetworkUtil.checkConnectedNetworkStatus(session.getContext()) == 0)
			throw new NoNetworkException();
		String s = session.getCookie();
		WalletHttpConnection wallethttpconnection = new WalletHttpConnection(
				session);
		HttpResult httpresult = wallethttpconnection.connect(httptask, s);
		int b = 0;
		if (httpresult.getHttpCode() == -4 || httpresult.getHttpCode() == 503
				|| httpresult.getHttpCode() == -8)
			throw new NoResponseException();
		if (httpresult.getHttpCode() == 401 && b++ < 1) {
			session.mobileLogin();
			return transmitRequest(httptask);
		}

		return httpresult;
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public RemoveAuthFailedRs removeAuthFailedService(
			final RemoveAuthFailedRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, RemoveAuthFailedRs.class, session.getServerUrl()
				+ "/spservice/deleteCustomerServiceSubscription",
				"serviceSubscriptionRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public RemoveNotExistLocalPresetServiceRs removeNotExistLocalPresetService(
			final RemoveNotExistLocalPresetServiceRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, RemoveNotExistLocalPresetServiceRs.class,
				session.getServerUrl()
						+ "/spservice/deleteCustomerServiceSubscription",
				"serviceSubscriptionRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public RecordSpOrderRs recordCustomerSpOrder(final RecordSpOrderRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, RecordSpOrderRs.class, session.getServerUrl()
				+ "/spservice/insertCustomerSpOrder", "customerSpOrderRq");
	}

}

package cn.unicompay.wallet.client.framework.api.http;

import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWoAccountRq;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWoAccountRs;
import cn.unicompay.wallet.client.framework.api.http.model.ChangeLanguageRq;
import cn.unicompay.wallet.client.framework.api.http.model.ChangePasswordRq;
import cn.unicompay.wallet.client.framework.api.http.model.ChangePasswordRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckChangeDeviceRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckChangeDeviceRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckSPDeviceAppSignatureRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckSPDeviceAppSignatureRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateWalletRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateWalletRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckWoAccountIdRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckWoAccountIdRs;
import cn.unicompay.wallet.client.framework.api.http.model.ContactUsRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetNoticeListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetNoticeListRs;
import cn.unicompay.wallet.client.framework.api.http.model.LoginWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.LoginWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.LogoutWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.LogoutWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.MobileAuthRq;
import cn.unicompay.wallet.client.framework.api.http.model.MobileAuthRs;
import cn.unicompay.wallet.client.framework.api.http.model.MobileStatusRq;
import cn.unicompay.wallet.client.framework.api.http.model.MobileStatusRs;
import cn.unicompay.wallet.client.framework.api.http.model.RegisterPushInfoRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestActivationCodeRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestActivationCodeRs;
import cn.unicompay.wallet.client.framework.api.http.model.RequestCustomerIdbyMobileUIDRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestCustomerIdbyMobileUIDRs;
import cn.unicompay.wallet.client.framework.api.http.model.RequestWalletVersionHistRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestWalletVersionHistRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResetWoPasswordRq;
import cn.unicompay.wallet.client.framework.api.http.model.ResetWoPasswordRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;
import cn.unicompay.wallet.client.framework.api.http.model.SendFeedbackRq;
import cn.unicompay.wallet.client.framework.api.http.model.SendFeedbackRs;
import cn.unicompay.wallet.client.framework.api.http.model.TnCRq;
import cn.unicompay.wallet.client.framework.api.http.model.TnCRs;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyLoginWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyLoginWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyUserIdRq;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyUserIdRs;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyWoSecurityQuestionRq;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyWoSecurityQuestionRs;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.http.gateway.BaseGateWay;

public class WalletGateWay extends BaseGateWay {
	/**
	 * @param networkMgr
	 */
	public WalletGateWay(NetworkManager networkMgr) {
		super(networkMgr);
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 * @throws com.skcc.wallet.core.http.exception.NoNetworkException
	 * @throws com.skcc.wallet.core.http.exception.NoResponseException
	 */
	public MobileAuthRs requestLoginWalletClient(final MobileAuthRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException,
			com.skcc.wallet.core.http.exception.NoNetworkException,
			com.skcc.wallet.core.http.exception.NoResponseException {
		return transmit(req, MobileAuthRs.class, session.getServerUrl()
				+ "/wallet/requestLoginWalletClient",
				"requestLoginWalletClientRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public LoginWalletClientRs reqLoginWalletClient(
			final LoginWalletClientRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, LoginWalletClientRs.class, session.getServerUrl()
				+ "/wallet/requestLoginWalletClient",
				"requestLoginWalletClientRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public MobileStatusRs reqMobileStatus(final MobileStatusRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, MobileStatusRs.class, session.getServerUrl()
				+ "/activate/requestActivateWalletClient",
				"requestActivateWalletClientRq");

	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public VerifyLoginWalletClientRs verifyLoginWalletClient(
			final VerifyLoginWalletClientRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, VerifyLoginWalletClientRs.class,
				session.getServerUrl() + "/wallet/verifyLoginWalletClient",
				"verifyLoginWalletClientRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public GetNoticeListRs getNoticeList(final GetNoticeListRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, GetNoticeListRs.class, session.getServerUrl()
				+ "/wallet/getNoticeList", "getNoticeListRq");
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ContactUsRs contactUs() throws NoNetworkException,
			NoResponseException, ResNotOKException {

		return transmit(emptyRq, ContactUsRs.class, session.getServerUrl()
				+ "/wallet/getContactUs", "getContactUsRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs registerPushInfo(final RegisterPushInfoRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/wallet/registerPushInfo", "registerPushInfoRq");
	}

	/**
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs resetWallet() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(emptyRq, ResultRs.class, session.getServerUrl()
				+ "/wallet/resetWallet", "resetWalletRq");

	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ResultRs changeLanguage(final ChangeLanguageRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResultRs.class, session.getServerUrl()
				+ "/wallet/changeLanguage", "changeLanguageRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public TnCRs getWalletTnC(final TnCRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {

		return transmit(req, TnCRs.class, session.getServerUrl()
				+ "/activate/getWoAccountTNC", "getWoAccountTNCRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public ActivateWalletClientRs activateWalletClient(
			final ActivateWalletClientRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, ActivateWalletClientRs.class,
				session.getServerUrl() + "/activate/activateWalletClient",
				"activateWalletClientRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public CheckUpdateWalletRs checkUpdateWallet(final CheckUpdateWalletRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, CheckUpdateWalletRs.class, session.getServerUrl()
				+ "/activate/checkUpdateWallet", "checkUpdateWalletRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public CheckChangeDeviceRs checkChangeDevice(final CheckChangeDeviceRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, CheckChangeDeviceRs.class, session.getServerUrl()
				+ "/activate/checkChangeDevice", "checkChangeDeviceRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 */
	public RequestCustomerIdbyMobileUIDRs requestCustomerIdbyMobileUID(
			final RequestCustomerIdbyMobileUIDRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, RequestCustomerIdbyMobileUIDRs.class,
				session.getServerUrl()
						+ "/activate/requestCustomerIdbyMobileUID",
				"requestCustomerIdbyMobileUIDRq");
	}

	/**
	 * @param req
	 * @return
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 * @author jin
	 * @throws ResNotOKException
	 * 
	 */
	public RequestActivationCodeRs requestActivationCode(
			final RequestActivationCodeRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, RequestActivationCodeRs.class,
				session.getServerUrl() + "/activate/requestActivationCode",
				"requestActivationCodeRq");
	}

	public ResetWoPasswordRs resetPassword(final ResetWoPasswordRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ResetWoPasswordRs.class, session.getServerUrl()
				+ "/wallet/forgetPassword/resetWoLoginPwd", "resetWoPasswordRq");
	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws NoNetworkException
	 * @throws NoResponseException
	 * @throws ResNotOKException
	 */
	public ChangePasswordRs changePassword(final ChangePasswordRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, ChangePasswordRs.class, session.getServerUrl()
				+ "/wallet/changePassword", "changePassword");

	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoNetworkException
	 * @throws NoResponseException
	 */
	public VerifyUserIdRs verifyUserId(final VerifyUserIdRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, VerifyUserIdRs.class, session.getServerUrl()
				+ "/wallet/forgetPassword/verifyUserId", "verifyUserIdRq");

	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoNetworkException
	 * @throws NoResponseException
	 */
	public CheckWoAccountIdRs checkWoAccountId(final CheckWoAccountIdRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		return transmit(req, CheckWoAccountIdRs.class, session.getServerUrl()
				+ "/wallet/forgetPassword/checkWoAccountId",
				"checkWoAccountIdRq");

	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws ResNotOKException
	 * @throws NoNetworkException
	 * @throws NoResponseException
	 */
	public ActivateWoAccountRs activateWoAccount(final ActivateWoAccountRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, ActivateWoAccountRs.class, session.getServerUrl()
				+ "/activate/activateWoAccount", "activateWoAccountRq");

	}

	public LogoutWalletClientRs logoutWalletClient(LogoutWalletClientRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		return transmit(req, LogoutWalletClientRs.class, session.getServerUrl()
				+ "/wallet/logoutWalletClient", "logoutWalletClientRq");
	}

	// After inputting Sequrity Question/Answer,
	public VerifyWoSecurityQuestionRs verifyWoSequrityQuestion(
			VerifyWoSecurityQuestionRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, VerifyWoSecurityQuestionRs.class,
				session.getServerUrl()
						+ "/wallet/forgetPassword/verifyWoSecurityQuestion",
				"verifyWoSecurityQuestionRq");
	}

	public SendFeedbackRs senFeedback(SendFeedbackRq req)
			throws NoNetworkException, NoResponseException, ResNotOKException {

		SendFeedbackRs rs = transmit(req, SendFeedbackRs.class,
				session.getServerUrl() + "/wallet/sendFeedback",
				"sendFeedbackRq");
		return rs;
	}

	public RequestWalletVersionHistRs getWalletVersionHistory(
			RequestWalletVersionHistRq req) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(req, RequestWalletVersionHistRs.class,
				session.getServerUrl() + "/wallet/requestWalletVersionHist",
				"requestWalletVersionHistRq");
	}

	public CheckSPDeviceAppSignatureRs checkSPDeviceAppSignature(
			CheckSPDeviceAppSignatureRq rq) throws NoNetworkException,
			NoResponseException, ResNotOKException {
		return transmit(
				rq,
				CheckSPDeviceAppSignatureRs.class,
				session.getServerUrl() + "/spservice/checkSPDeviceAppSignature",
				"checkSPDeviceAppSignatureRq");

	}
}

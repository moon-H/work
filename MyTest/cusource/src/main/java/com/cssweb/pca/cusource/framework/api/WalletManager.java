package cn.unicompay.wallet.client.framework.api;

// import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.api.http.SpAppGateWay;
import cn.unicompay.wallet.client.framework.api.http.WalletGateWay;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWoAccountRq;
import cn.unicompay.wallet.client.framework.api.http.model.ActivateWoAccountRs;
import cn.unicompay.wallet.client.framework.api.http.model.ChangePasswordRq;
import cn.unicompay.wallet.client.framework.api.http.model.ChangePasswordRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckEligibilityRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckSPDeviceAppSignatureRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckSPDeviceAppSignatureRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateWalletRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateWalletRs;
import cn.unicompay.wallet.client.framework.api.http.model.CheckWoAccountIdRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckWoAccountIdRs;
import cn.unicompay.wallet.client.framework.api.http.model.ClientInfo;
import cn.unicompay.wallet.client.framework.api.http.model.GetSpListRs;
import cn.unicompay.wallet.client.framework.api.http.model.InstallSpAppRq;
import cn.unicompay.wallet.client.framework.api.http.model.LoginWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.LoginWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.LogoutWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.LogoutWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.MobileStatusRq;
import cn.unicompay.wallet.client.framework.api.http.model.MobileStatusRs;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveNotExistLocalPresetServiceRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestActivationCodeRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestActivationCodeRs;
import cn.unicompay.wallet.client.framework.api.http.model.RequestCustomerIdbyMobileUIDRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestCustomerIdbyMobileUIDRs;
import cn.unicompay.wallet.client.framework.api.http.model.RequestWalletVersionHistRq;
import cn.unicompay.wallet.client.framework.api.http.model.RequestWalletVersionHistRs;
import cn.unicompay.wallet.client.framework.api.http.model.ResetWoPasswordRq;
import cn.unicompay.wallet.client.framework.api.http.model.ResetWoPasswordRs;
import cn.unicompay.wallet.client.framework.api.http.model.Result;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;
import cn.unicompay.wallet.client.framework.api.http.model.SendFeedbackRq;
import cn.unicompay.wallet.client.framework.api.http.model.SendFeedbackRs;
import cn.unicompay.wallet.client.framework.api.http.model.TnCRq;
import cn.unicompay.wallet.client.framework.api.http.model.TnCRs;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyLoginWalletClientRq;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyLoginWalletClientRs;
import cn.unicompay.wallet.client.framework.api.http.model.VerifyUserIdRq;
import cn.unicompay.wallet.client.framework.api.http.model.ViewCategoryRs;
import cn.unicompay.wallet.client.framework.db.MySpServiceDB;
import cn.unicompay.wallet.client.framework.dm.DMService;
import cn.unicompay.wallet.client.framework.model.Category;
import cn.unicompay.wallet.client.framework.model.SP;
import cn.unicompay.wallet.client.framework.model.SpService;
import cn.unicompay.wallet.client.framework.util.DeviceInfo;
import cn.unicompay.wallet.client.framework.util.Util;
import cn.unicompay.wallet.client.framework.view.DialogMessage;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.se.SEConnectionListener;
import com.skcc.wallet.core.se.SException;
import com.skcc.wallet.core.se.SExceptionInfo;
import com.skcc.wallet.core.se.instance.CRSApplication;
import com.skcc.wallet.core.se.instance.PINBlockedException;
import com.skcc.wallet.core.se.util.HexString;

/**
 * <pre>
 * Description for SP service application install/update/remove steps as below :
 * 
 * - SP remove : 1.dialog(view) -> 2.WalletManager#showRemoveDialog() -> 3.Android Platform -> 4.DMReceiver -> 5.Server -> 6.MQTTService -> 7.PushService -> 8.receiver(view)
 * 
 * - SP install : 1.dialog(view) -> 2.Server -> 3.MQTTService -> 4,PushService -> 5.receiver(view) -> 6.WalletManager#requestInstallUpdateApp() -> 7.DMService -> 8.Android Platform -> 9.DMReceiver -> 10.PushService -> 11.receiver(view) 
 * 
 * - SP update : 1.SPServiceManager#checkUpdateService() -> 2.WalletManager#requestInstallUpdateApp() -> 3.DMService -> 4.Android Platform -> 5.DMReceiver -> receiver(view)
 * 
 * In case of both SP update and remove, the view just requests framework for it, and get the result by receiver. 
 * If user selects to cancel it, the result can be checked by calling startActivityForResult().
 * In SP install, view can get the download URL from receiver(step 5),delivers it to framework and then get the result from receiver.
 * </pre>
 * 
 * @author SKCCADMIN
 * 
 */
public class WalletManager {

	private static final String TAG = "WalletManager";
	private static final String WALLETID = "000001";
	private static final int WALLET_SUBSCRIPTION_STATE_NO_REGISTRATION = 21000;
	private static final int WALLET_SUBSCRIPTION_STATE_PRE_ACTIVATED = 21006;
	private static final int WALLET_SUBSCRIPTION_STATE_ACTIVATED = 21001;
	private static final int WALLET_SUBSCRIPTION_STATE_PRE_TERMINATED = 21007;
	private static final int WALLET_SUBSCRIPTION_STATE_TERMINATED = 21008;
	// private static final int WALLET_SUBSCRIPTION_STATE_LOCKED = 21003;
	private static final int WO_ACCOUNT_STATE_NO_REGISTRATION = -1;
	private static final int WO_ACCOUNT_STATE_PRE_ACTIVATED = 0;
	private static final int WO_ACCOUNT_STATE_ACTIVATED = 1;
	// private static final int WO_ACCOUNT_STATE_PRE_TERMINATED = 8;
	private static final int WO_ACCOUNT_STATE_TERMINATED = 9;
	private static final String WO_ACCOUNT_LOGIN_SUCCESS = "true";
	private static final String WALLET_SUBSCRIPTION_STATE = "WALLET_SUBSCRIPTION_STATE";
	private static final String WO_ACCOUNT_STATE = "WO_ACCOUNT_STATE";
	public static final int NO_UPDATE = 0;
	public static final int OPTIONAL_UPDATE = 1;
	public static final int MANDATORY_UPDATE = 2;
	public static final String HRV_VALUE = "hrv_value";
	public static final String WO_ACCOUNT_ID = "wo_account_id";
	public static final String WALLET_STATUS_KEY = "wallet_status";
	// private static final String WALLET_LANGUAGE_KEY = "wallet_lanaguage";
	private static final String WALLET_ICCID_KEY = "wallet_iccid";
	public static final String ACTION_MAIN_POST = ".ACTION_SP_MAIN";
	/** Receiver action name for installing **/
	public final static String SP_APP_INSTALL_ACTION = "cn.unicompay.wallet.client.framework.SP_APP_INSTALL_ACTION";
	/** Receiver action name for deleting **/
	public final static String SP_APP_DELETE_ACTION = "cn.unicompay.wallet.client.framework.SP_APP_DELETE_ACTION";
	/** Receiver action name for updating **/
	public final static String SP_APP_UPDATE_ACTION = "cn.unicompay.wallet.client.framework.SP_APP_UPDATE_ACTION";
	/** Receiver extra name for app ID **/
	public final static String EXTRA_SP_APP_ID = "cn.unicompay.wallet.client.framework.EXTRA_SP_APP_ID";
	/** Receiver extra name for package name **/
	public final static String EXTRA_SP_PACKACE_NAME = "cn.unicompay.wallet.client.framework.EXTRA_SP_PACKACE_NAME";
	/** Receiver extra name for result which is success or fail **/
	public final static String EXTRA_SP_OP_RESULT = "cn.unicompay.wallet.client.framework.EXTRA_SP_OP_RESULT";
	/** Receiver action name for installing wma **/
	public final static String WALLET_INSTALL_ACTION = "cn.unicompay.wallet.client.framework.WALLET_INSTALL_ACTION";
	/** Receiver action name for resetting wallet **/
	public final static String WALLET_DELETE_ACTION = "cn.unicompay.wallet.client.framework.WALLET_DELETE_ACTION";
	/** Receiver action name for unblocking pin **/
	public final static String UNBLOCK_PIN_ACTION = "cn.unicompay.wallet.client.framework.UNBLOCK_PIN_ACTION";
	/** Receiver extra name for application download URL **/
	public final static String EXTRA_SP_DOWNLOAD_URL = "cn.unicompay.wallet.client.framework.EXTRA_SP_DOWNLOAD_URL";
	/** Receiver action name for being installed **/
	public final static String SP_APP_INSTALLED_ACTION = "cn.unicompay.wallet.client.framework.SP_APP_INSTALLED_ACTION";
	/** Receiver action name for wallet updating **/
	public final static String WALLET_UPDATE_ACTION = "cn.unicompay.wallet.client.framework.WALLET_UPDATE_ACTION";
	public final static String EXTRA_UPDATE_RESULT = "cn.unicompay.wallet.client.framework.EXTRA_UPDATE_RESULT";
	public final static String ERROR_MOBILEID = "0000000000000000";
	public final static String WMA_AID = "F0000000000191452F02030100000081";
	public final static String ACTION_EXCUTE_BY_SP_LOGIN_N = "cn.unicompay.wallet.ACTION_EXCUTE_BY_SP_NO_LOGIN";
	public final static String ACTION_EXCUTE_BY_SP_LOGIN_Y = "cn.unicompay.wallet.ACTION_EXCUTE_BY_SP_LOGIN";
	public final static String ETICKET_AID = "eticket_aid";
	public final static String ETICKET_SERVICEID = "eticket_serviceId";
	public final static String ETICKET_APDU = "eticket_apdu";
	public final static String ETICKET_SUCCESSMSG = "eticket_successMsg";
	public final static String ETICKET_FAILMSG = "eticket_failMsg";
	public final static String ETICKET_ORDERID = "eticket_orderId";
	/**
	 * Request code for deleting SP Service application
	 */
	public static final int ACTION_DELETE_REQUEST = 101;
	/**
	 * Request code for installing SP Service application
	 */
	public static final int ACTION_INSTALL_REQUEST = 102;
	/**
	 * Request code for updating SP Service application
	 */
	public static final int ACTION_UPDATE_REQEUST = 103;
	/**
	 * Request code for updating wallet
	 */
	public static final int ACTION_WALLET_UPDATE_REQUEST = 104;
	private WApplication context;
	private String appId;
	/**
	 * 
	 */
	public static final String NOT_ALLOWED_ACCESS_TO_SPECIFIED_AID = "not allowed access to specified AID";
	public static final String CONNECTION_REFUSED = "Connection refused !!!";
	public static final String APPLET_WITH_THE_DEFINED_AID_DOES_NOT_EXIST_IN_THE_SE = "Applet with the defined aid does not exist in the SE";
	public static final String ACCESS_CONTROL_ENFORCER = "Access Control Enforcer: no APDU access allowed!";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            android context
	 * @param appId
	 *            application Id
	 */
	public WalletManager(WApplication context, String appId) {
		this.context = context;
		this.appId = appId;
	}

	/**
	 * <pre>
	 * Handles the process before moving to main screen.
	 * There are some tasks processed as below:
	 * <li> mobile log-in.
	 * <li> get default and missing applications.
	 * <li> updates local database.
	 * <li> get and save messages in box.
	 * </pre>
	 * 
	 * @param l
	 * @see LoginListener
	 */
	public void login(final String userId, final String userPassword,
			final LoginListener l) {
		new Thread(new Runnable() {

			public void run() {
				if (!context.getNetworkManager().isNetworkAvailable()) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				}
				WalletGateWay wallet = context.getNetworkManager()
						.getWalletGateWay();
				DeviceInfo device = new DeviceInfo(context);
				LoginWalletClientRq req = new LoginWalletClientRq();
				req.setMobileUniqueId(device.getICCID());
				req.setMobileUidType("ICCID");
				req.setWoAccountId(userId);
				req.setWoAccountPwd(userPassword);
				final LoginWalletClientRs res;
				try {
					res = wallet.reqLoginWalletClient(req);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								String mobileId = null;
								try {
									mobileId = context.getSEManager()
											.getMobileId();
								} catch (Exception e) {
									e.printStackTrace();
									mobileId = null;
								}
								Log.d(TAG, "MobileID>>>login>>>>>>>:"
										+ mobileId);
								if (res.isWoAccountLoginSuccess()
										&& mobileId != null
										&& !mobileId.equals(ERROR_MOBILEID)) {
									context.setWoAccountId(userId);
									context.getSettingManager().putString(
											WO_ACCOUNT_ID, userId);
									context.setHRV(res.getHRV());
									l.onFinished();
								} else {
									l.onNoSE();
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onLoginFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	public void activate(final String userId, final ActivationListener l) {
		new Thread(new Runnable() {

			public void run() {
				// mobile login
				try {
					final MobileStatusRs result = requestMobileStatus(userId);
					Log.d(TAG,
							"activate>> Result for getting mobile status :: "
									+ result);
					if (result != null
							&& result.getResult().getCode() == Result.OK) {
						String mobileId = null;
						try {
							mobileId = context.getSEManager().getMobileId();
						} catch (Exception e) {
							e.printStackTrace();
							mobileId = null;
						}
						// wallet account status is activate, wo account status
						// is
						// activate, WMA status is perso
						// result.setWoAccountState(WO_ACCOUNT_STATE_PRE_ACTIVATED);
						// result.setWalletSubscriptionState(WALLET_SUBSCRIPTION_STATE_PRE_ACTIVATED);
						if (result.getWalletSubscriptionState() == WALLET_SUBSCRIPTION_STATE_ACTIVATED
								&& result.getWoAccountState() == WO_ACCOUNT_STATE_ACTIVATED
								&& mobileId != null
								&& !mobileId.equals(ERROR_MOBILEID)) {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									if (l != null) {
										l.onFinished();
									}
								}
							});
						} else {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									if (l != null) {
										context.getSettingManager()
												.putInt(WALLET_SUBSCRIPTION_STATE,
														result.getWalletSubscriptionState());
										context.getSettingManager().putInt(
												WO_ACCOUNT_STATE,
												result.getWoAccountState());
										l.onActivationNeeded();
									}
								}
							});
						}
					} else {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (l != null && result != null) {
									l.onGettingStateFailed(result.getResult()
											.getCode(), result.getResult()
											.getMessage());
								}
							}
						});
					}
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onNoNetwork();
							}
						}
					});
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onNoResponse();
							}
						}
					});
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onGettingStateFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	public void activateWalletAccount(final WalletStateListener l) {
		new Thread(new Runnable() {

			public void run() {
				int walletStatus = context.getSettingManager().getInt(
						WALLET_SUBSCRIPTION_STATE, 0);
				if (walletStatus == WALLET_SUBSCRIPTION_STATE_NO_REGISTRATION) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWalletNotRegistered();
							}
						}
					});
					return;
				} else if (walletStatus == WALLET_SUBSCRIPTION_STATE_PRE_ACTIVATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWalletPreActivated();
							}
						}
					});
					return;
				} else if (walletStatus == WALLET_SUBSCRIPTION_STATE_ACTIVATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWalletActivated();
							}
						}
					});
					return;
				} else if (walletStatus == WALLET_SUBSCRIPTION_STATE_PRE_TERMINATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
							}
						}
					});
					return;
				} else if (walletStatus == WALLET_SUBSCRIPTION_STATE_TERMINATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
							}
						}
					});
					return;
				}
			}
		}).start();
	}

	public void provisioningWma(final WmaStateListener l) {
		Log.d(TAG, "check WMA>>>>");
		new Thread(new Runnable() {

			public void run() {
				String mobileId = null;
				boolean needProvisionWma = false;
				try {
					context.getSEManager().clearMobileId();
					mobileId = context.getSEManager().getMobileId();
					if (mobileId == null) {
						Log.d(TAG, "mobiledId is null");
					}
					Log.d(TAG, "mobileID Size >>>>:" + mobileId.length());
					Log.d(TAG, "provisioningWma>>mobileId>>>>>>>" + mobileId);
				} catch (NoSuchElementException nse) {
					nse.printStackTrace();
					CRSManager crsManager = context.getSEManager()
							.getCrsManager();
					CRSApplication.Application app = null;
					try {
						app = crsManager.getApp(WMA_AID);
						if (app != null) {
							Util.makeLooperThread(new Runnable() {

								@Override
								public void run() {
									l.onWmaLocked();
								}
							});
							return;
						}
					} catch (SException e1) {
						// 没有WMA
						Log.d(TAG, "getMobileID.errCode"
								+ e1.getInformation().errCode);
						e1.printStackTrace();
						if (e1.getInformation().errCode == SExceptionInfo.REFERECED_DATA_NOT_FOUND) {
							needProvisionWma = true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage() != null
							&& e.getMessage()
									.equals(APPLET_WITH_THE_DEFINED_AID_DOES_NOT_EXIST_IN_THE_SE)) {
						CRSManager crsManager = context.getSEManager()
								.getCrsManager();
						CRSApplication.Application app = null;
						try {
							app = crsManager.getApp(WMA_AID);
							if (app != null) {
								Util.makeLooperThread(new Runnable() {

									@Override
									public void run() {
										l.onWmaLocked();
									}
								});
								return;
							}
						} catch (SException e1) {
							// 没有WMA
							Log.d(TAG,
									"getMobileID.errCode"
											+ e1.getInformation().errCode);
							e1.printStackTrace();
							if (e1.getInformation().errCode == SExceptionInfo.REFERECED_DATA_NOT_FOUND) {
								needProvisionWma = true;
							}
						}
					} else if (e.getMessage() != null
							&& (e.getMessage().equals(
									NOT_ALLOWED_ACCESS_TO_SPECIFIED_AID)
									|| e.getMessage()
											.equals(CONNECTION_REFUSED) || e
									.getMessage().equals(
											ACCESS_CONTROL_ENFORCER))) {
						// 没有ACF
						needProvisionWma = true;
					} else {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.onNoSE();
							}
						});
						return;
					}
				}
				if (needProvisionWma || mobileId == null
						|| mobileId.trim().length() == 0
						|| mobileId.equals(ERROR_MOBILEID)) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWmaState(false);
							}
						}
					});
				} else {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWmaState(true);
							}
						}
					});
				}
				return;
			}
		}).start();
	}

	public void activateWoAccount(final WoStateListener l) {
		new Thread(new Runnable() {

			public void run() {
				int woStatus = context.getSettingManager().getInt(
						WO_ACCOUNT_STATE, 0);
				if (woStatus == WO_ACCOUNT_STATE_NO_REGISTRATION) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWoNotRegistered();
							}
						}
					});
					return;
				} else if (woStatus == WO_ACCOUNT_STATE_PRE_ACTIVATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWoPreActivated();
							}
						}
					});
					return;
				} else if (woStatus == WO_ACCOUNT_STATE_ACTIVATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onWoActivated();
							}
						}
					});
					return;
				} else if (woStatus == WO_ACCOUNT_STATE_TERMINATED) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null) {
								l.onTerminated();
							}
						}
					});
					return;
				}
			}
		}).start();
	}

	public void checkMobileAuth(final AuthListener l) {
		new Thread(new Runnable() {

			public void run() {
				final boolean successAuth = moblieAuth();
				Log.d(TAG, " Result for mobile log-in :: " + successAuth);
				Util.makeLooperThread(new Runnable() {

					public void run() {
						if (!successAuth) {
							l.onAuthFail();
						} else {
							l.onAuthSuccess();
						}
					}
				});
			}
		}).start();
		return;
	}

	public void syncData(final SyncDataListener l) {
		new Thread(new Runnable() {

			public void run() {
				// Get Category
				SpAppGateWay spGw = context.getNetworkManager()
						.getSpAppGateWay();
				ViewCategoryRs categories;
				try {
					categories = spGw.viewCategory();
					if (categories != null) {
						Vector<Category> listOnLine = categories
								.getCategoryList();
						context.getSpServiceManager().setSPCategory(listOnLine);
					} else {
						Log.e(TAG, " categories is null!!");
					}
					// Get SP
					GetSpListRs spList = spGw.getSpList();
					if (spList != null) {
						Vector<SP> listOnLine = spList.getSpList(); // 네트웍
																	// 통신
						context.getSpServiceManager().setSPList(listOnLine);
					} else {
						Log.e(TAG, " SP list is null!!");
					}
				} catch (NoNetworkException e1) {
					e1.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e1) {
					e1.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
				// get default app.
				SPServiceManager spAppMgr = context.getSpServiceManager();
				// spAppMgr.removeAll();
				Vector<SpService> listOnLine = null;
				try {
					listOnLine = spAppMgr.getMySpAppListOnStore();
				} catch (NoNetworkException e1) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e1) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} // 네트웍
				catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
				// 통신
				Vector<SpService> listOnPhone = spAppMgr.getMySpAppList(
						MySpServiceDB.CATEGORY_ALL_ID,
						MySpServiceDB.SORT_BY_DEFAULT);
				Vector<SpService> updateList = new Vector<SpService>();
				Vector<SpService> insertList = new Vector<SpService>();
				Log.d(TAG, " current status ::");
				if (listOnLine != null) {
					Log.d(TAG, " listOnLine ::" + listOnLine.size());
				} else {
					Log.d(TAG, " listOnLine :: null");
				}
				if (listOnPhone != null) {
					Log.d(TAG, " listOnPhone ::" + listOnPhone.size());
				}
				// -----------------------------
				// 删除本地数据库有，服务器上没有的记录。
				Vector<SpService> removeList = new Vector<SpService>();
				if (listOnPhone.size() != 0) {
					for (SpService app1 : listOnPhone) {
						boolean findApp = false;
						if (listOnLine != null) {
							for (SpService app2 : listOnLine) {
								if (app1.getServiceId().equals(
										app2.getServiceId())) {
									findApp = true;
								}
							}
							if (findApp == false) {
								removeList.add(app1);
							}
						} else {
							removeList.add(app1);
						}
					}
				}
				if (removeList.size() > 0) {
					for (SpService app : removeList) {
						Log.d(TAG, " delete :: " + app.getServiceName());
						spAppMgr.remove(app);
					}
				}
				removeList.clear();
				// -----------------------------------------
				if (listOnLine != null) {
					for (SpService app1 : listOnLine) {
						boolean findApp = false;
						if (listOnPhone != null) {
							for (SpService app2 : listOnPhone) {
								if (app1.getServiceId().equals(
										app2.getServiceId())) {
									// app1.setAppletState(app1
									// .getServiceSubscriptionState());
									// 需要更新时不能用这个serviceVersion比较，具体用哪个字段需要重新跟次长确定。暂时先不比较。
									// if (app1.getServiceVersion().equals(
									// app2.getServiceVersion())) {
									// has nothing to do.
									// } else {
									// 本地有，版本不同，更新数据
									updateList.add(app1);
									// }
									listOnPhone.remove(app1);
									findApp = true;
									break;
								}
							}
						}
						if (findApp == false) {
							// 本地没有，添加到数据库
							insertList.add(app1);
						}
					}
					// if(listOnPhone.size()>0) {
					// for(SpApp app:listOnPhone) {
					// removeList.add(app);
					// }
					// }
					listOnLine.clear();
				}
				listOnPhone.clear();
				if (insertList.size() > 0) { // db insert
					SpService service;
					for (SpService app : insertList) {
						try {
							service = spAppMgr.getSpAppInfo(app.getServiceId()); // 네트웍//
																					// 통신
							if (service != null) {
								service.setServiceSubscriptionState(app
										.getServiceSubscriptionState());
								Log.d(TAG,
										" pre insert service :: "
												+ service.getServiceName());
								context.getSpServiceManager().insert(service);
							}
						} catch (NoNetworkException e) {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onNoNetwork();
								}
							});
							return;
						} catch (NoResponseException e) {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onNoResponse();
								}
							});
							return;
						} catch (final ResNotOKException e) {
							e.printStackTrace();
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onFailed(e.getCode(), e.getMessage());
								}
							});
							return;
						}
					}
				}
				if (updateList.size() > 0) {// db update
					SpService service;
					for (SpService app : updateList) {
						try {
							service = spAppMgr.getSpAppInfo(app.getServiceId()); // 네트웍//
																					// 통신
							if (service != null) {
								service.setServiceSubscriptionState(app
										.getServiceSubscriptionState());
								Log.d(TAG,
										" pre Update service :: "
												+ service.getServiceName());
								int i = spAppMgr.update(service); //
								Log.d(TAG, "更新了几条数据>>>>>>>>>>：" + i);
							}
						} catch (NoNetworkException e) {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onNoNetwork();
								}
							});
							return;
						} catch (NoResponseException e) {
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onNoResponse();
								}
							});
							return;
						} catch (final ResNotOKException e) {
							e.printStackTrace();
							Util.makeLooperThread(new Runnable() {

								public void run() {
									l.onFailed(e.getCode(), e.getMessage());
								}
							});
							return;
						}
					}
					// for (SpService app : updateList) {
					// int i = spAppMgr.update(app); //
					// Log.d(TAG, "更新了几条数据>>>>>>>>>>：" + i);
					// }
				}
				insertList.clear();
				updateList.clear();
				// check missing gui app
				Vector<String> appletAids = context.getSEManager()
						.getCrsManager().getAllAid();
				Vector<SpService> guiAppList = null;
				try {
					guiAppList = spAppMgr.getGuiSpAppListOnStore(appletAids);
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} // 네트웍 통신
				catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
				listOnPhone = spAppMgr.getMySpAppList(
						MySpServiceDB.CATEGORY_ALL_ID,
						MySpServiceDB.SORT_BY_DEFAULT);
				Log.d(TAG, "listOn>>>gui>>" + listOnPhone.size());
				if (appletAids != null)
					Log.d(TAG, " appletAids :: " + appletAids.size());
				if (guiAppList != null)
					Log.d(TAG, " guiAppList :: " + guiAppList.size());
				boolean isExist = false;
				List<String> deleteServiceIdList = new ArrayList<String>();
				if (listOnPhone != null) {
					for (SpService app1 : listOnPhone) {
						isExist = false;
						if (SpService.PRESET_SERVICE == app1.getServiceType()) {
							for (String aid : appletAids) {
								if (guiAppList != null) {
									if (aid.equals(app1.getAppletAid())) {
										isExist = true;
										break;
									}
								}
							}
							if (isExist == false) {
								Log.d(TAG,
										" pre remove extra :: "
												+ app1.getServiceId());
								spAppMgr.remove(app1);
								deleteServiceIdList.add(app1.getServiceId());
							}
						}
					}
				}
				// 删除卡片上没有的预置应用
				if (deleteServiceIdList.size() != 0) {
					RemoveNotExistLocalPresetServiceRq rq = new RemoveNotExistLocalPresetServiceRq();
					rq.setServiceIdList(deleteServiceIdList);
					rq.setWoAccountId(context.getWoAccountId());
					try {
						spGw.removeNotExistLocalPresetService(rq);
					} catch (NoNetworkException e) {
						e.printStackTrace();
					} catch (NoResponseException e) {
						e.printStackTrace();
					} catch (ResNotOKException e) {
						e.printStackTrace();
					}
				}
				// update applet status by comparing with CRS
				try {
					context.getSpServiceManager().refreshSpAppletStatus();
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoSE();
						}
					});
					return;
				}
				// get and save message in box.
				try {
					context.getInBoxManager().getMyInBoxMsg();
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} // 네트웍 통신
				catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
				// set loginstate
				Log.d(TAG,
						"is Illegal login state>>:" + context.isIllegalUser());
				context.setIllegalUser(false);

				// -------------------------------
				// 如果有去哪儿网信息，则将去哪儿网信息写入相应应用。
				if (getEticketData().get(ETICKET_AID) != null) {
					Log.d(TAG, "prepare to write 去哪儿 info>>>");
					Log.d(TAG, "去哪信息>>>>>>>" + getEticketData().toString());
					CRSManager crsManager = context.getSEManager()
							.getCrsManager();
					crsManager.getApplicationByAid(
							new getApplicationListener() {
								@Override
								public void onExist() {
									// 开始写卡
									// 设置Context的aid
									Log.d(TAG, "qu na Exist>>>>>>");
									context.setAid(HexString
											.hexStringToBytes(getEticketData()
													.get(ETICKET_AID)));
									context.getSEManager()
											.openSEChannel(
													HexString
															.hexStringToBytes(getEticketData()
																	.get(ETICKET_AID)));
									byte[] response = context
											.getSEManager()
											.transceiveAPDU(
													HexString
															.hexStringToBytes(getEticketData()
																	.get(ETICKET_APDU)));
									if (HexString.bytesToHexString(response)
											.endsWith("9000")) {
										context.getSEManager().closeSEChannel();
										context.setEticket_status("01");
										recordCustomerSpOrder("Y");
									} else {
										context.getSEManager().closeSEChannel();
										context.setEticket_status("02");
										recordCustomerSpOrder("N");
									}
								}

								@Override
								public void notExist() {
									// 应用不存在
									Log.d(TAG, "Applet not Exist>>>>");
									context.setEticket_status("03");
								}

								@Override
								public void noSE() {
									Log.d(TAG,
											"write 去哪儿 fail:noSE>>>>>>>>>>>>>>>>");
									context.setEticket_status("02");
								}
							}, getEticketData().get(ETICKET_AID));
				} else {
					Log.d(TAG, "No quna Info>>>>>>>>>");
				}
				// -------------------------------
				Util.makeLooperThread(new Runnable() {

					public void run() {
						if (l != null) {
							l.onFinishedSync();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * Handle mobile log in.
	 * 
	 * @return true if success
	 * 
	 */
	boolean moblieAuth() {
		WalletGateWay wallet = context.getNetworkManager().getWalletGateWay();
		String hrv = context.getHRV();
		// String hrv = "";
		try {
			context.getSEManager().makeCryptogram(hrv);
		} catch (SException e1) {
			Log.d(TAG, "SException>>>>>>>>>");
			e1.printStackTrace();
			context.getSEManager().closeSEChannel();
			return false;
		} catch (PINBlockedException e1) {
			Log.d(TAG, "PINBlockedException>>>>>>>>>");
			e1.printStackTrace();
			context.getSEManager().closeSEChannel();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		String mobileId = null;
		try {
			mobileId = context.getSEManager().getMobileId();
			Log.d(TAG, "mobileID>>>>>>>>" + mobileId);
		} catch (Exception e1) {
			e1.printStackTrace();
			mobileId = null;
		}
		VerifyLoginWalletClientRq vreq = new VerifyLoginWalletClientRq();
		vreq.setClientInfo(new ClientInfo(mobileId, Locale.getDefault()
				.getLanguage()));
		vreq.setcMAC(context.getSEManager().getCMAC());
		vreq.setCRV(context.getSEManager().getCRV());
		VerifyLoginWalletClientRs vres = null;
		try {
			vres = wallet.verifyLoginWalletClient(vreq);
			// -----------------------------------
			context.setName(vres.getRealName());
			context.setIdType(vres.getUserIdType());
			context.setIdValue(vres.getUserId());
			context.setPhoneNo(context.getWoAccountId());
			// ------------------------------------
			if (vres != null && vres.gethMAC() != null
					&& vres.getResult().getCode() == Result.OK) {
				Log.d(TAG, "双向认证了>>>>>>>>>>>>>>>>");

				// 客户端不认证服务器
				context.getSEManager().closeSEChannel();
				return true;
				/**
				 * 上面注释：关闭认证服务器，下面注释：开启双向认证。
				 * */
				// boolean b = context.getSEManager().confirmCryptogram(
				// vres.gethMAC());
				// Log.d(TAG, "认证结果>>>>>>>>>>>>>>>>>>>>>>" + b);
				// return b;
			} else {
				Log.d(TAG, "认证结果>>>>>>HMAC可能空了>>>>>>>>>>>>>>>>");
				context.getSEManager().closeSEChannel();
				return true;// 关闭双向
				// return false;// 开启双向
			}
		} catch (Exception e) {
			context.getSEManager().closeSEChannel();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Handle mobile status.
	 * 
	 * @return true if success
	 * @throws NoResponseException
	 * @throws NoNetworkException
	 * @throws ResNotOKException
	 */
	MobileStatusRs requestMobileStatus(String userId)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		WalletGateWay wallet = context.getNetworkManager().getWalletGateWay();
		DeviceInfo device = new DeviceInfo(context);
		String iccid = device.getICCID();
		MobileStatusRq req = new MobileStatusRq();
		req.setMobileUniqueId(iccid);
		req.setMobileUidType("ICCID");
		req.setWoAccountId(userId);
		MobileStatusRs res = wallet.reqMobileStatus(req);
		return res;
	}

	/**
	 * Checks if there is a update of wallet.
	 * 
	 * @param l
	 */
	public void checkWalletUpdate(final WalletUpdateListener l) {
		new Thread(new Runnable() {

			public void run() {
				final char[] updateUrl = new char[255];
				if (context.getNetworkManager().isNetworkAvailable() == false) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				}
				DeviceInfo device = new DeviceInfo(context);
				// CheckUpdateWalletRq req = new CheckUpdateWalletRq(WALLETID,
				// getWalletVersion(), device.getIMEI());
				CheckUpdateWalletRq req = new CheckUpdateWalletRq();
				req.setAppVersionName(getWalletVersion());
				req.setImei(device.getIMEI());
				req.setMobileUidType("ICCID");
				req.setMobileUniqueId(device.getICCID());// iccid
				req.setModelName(device.getDeviceModelName());// 手机型号
				req.setWalletId(WALLETID);

				Log.d(TAG, "checkWalletUpdate>> appId:" + req.getWalletId()
						+ ",version:" + req.getAppVersionName());
				WalletGateWay wallet = context.getNetworkManager()
						.getWalletGateWay();
				try {
					final CheckUpdateWalletRs res = wallet
							.checkUpdateWallet(req);

					int result = NO_UPDATE;
					if (res != null && "Y".equalsIgnoreCase(res.getUpdateYn())) {
						char[] src = res.getAppDownloadUrl().toCharArray();
						System.arraycopy(src, 0, updateUrl, 0,
								Math.min(src.length, updateUrl.length));
						if ("Y".equalsIgnoreCase(res.getUpdateMandatoryYn())) {
							result = MANDATORY_UPDATE;
						} else {
							result = OPTIONAL_UPDATE;
						}
					}
					Log.d(TAG,
							"URL>>>>>>>>>>>>>" + new String(updateUrl).trim());
					if (result == MANDATORY_UPDATE) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onMandatoryUpdate(
										new String(updateUrl).trim(),
										res.getAppversionDesc());
							}
						});
					} else if (result == OPTIONAL_UPDATE) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onOptionalUpdate(
										new String(updateUrl).trim(),
										res.getAppversionDesc());
							}
						});
					} else {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoUpdate();
							}
						});
					}
				} catch (NoNetworkException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Request update if there is update url.
	 * 
	 * @param activity
	 * @param updateUrl
	 * @throws DiskNotReadyException
	 */
	public void requestUpdateWallet(final Activity activity,
			final String updateUrl) throws DiskNotReadyException {
		if (DMService.isWritableExternalDisk() == false)
			throw new DiskNotReadyException();
		DMService dmSvc = new DMService(activity, updateUrl, null,
				WALLET_UPDATE_ACTION);
		dmSvc.start();
	}

	/**
	 * <pre>
	 * Be cautions! The prior condition is that the connection for SE must be done.
	 * This method does things as below:
	 * <li> Prepares for SE communications including checking SE's status.
	 * <li> Checking network's status.
	 * <li> Checking phone change event.
	 * <li> Checking wallet's status if it is under being reset or activating.
	 * <li> Starting a push service.
	 * <li> Getting a saved mobile ID.
	 * After the method being called, you must call update().
	 * Under specific circumstances, the method of LauncherListener will be called.
	 * </pre>
	 * 
	 * @param l
	 * @see LauncherListener
	 * @see SEManager#connect()
	 */
	public void start(final LauncherListener l) {
		new Thread(new Runnable() {

			public void run() {
				if (TextUtils.isEmpty(context.getSettingManager().getString(
						NetworkManager.WALLET_PACKAGE_NAME, ""))) {
					writeWalletPkg();
				}
				if (context.getSEManager().hasSEConnection() == false) {
					context.getSEManager().setSEConnectionListener(
							new SEConnectionListener() {

								@Override
								public void onSEConnectionFail() {
									context.getSEManager()
											.setSEConnectionListener(null);
									l.onNoSE();
								}

								@Override
								public void onSEConnected() {
									context.getSEManager()
											.setSEConnectionListener(null);
									// new Thread(new Runnable() {
									// public void run() {
									// checkPreStatus(l);
									// }
									// }).start();
									Log.d(TAG,
											"onSEConnected>>>>>>>>>>>>>>>>>>>>>");
								}

								@Override
								public void onSEDisconnected() {
									Log.d(TAG,
											"SEDisconnected>>>>>>>>>>>>>>>>>>>>>");
								}
							});
					context.getSEManager().connect();
					// return;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkPreStatus(l);
			}
		}).start();
	}

	private void checkPreStatus(final LauncherListener l) {
		Log.d(TAG, "startInThread>> start push service ::");
		// start push agent
		// MQTTPushService.actionStart(context);
		// check SE status.
		SEManager se = context.getSEManager();
		if (!se.hasSEConnection()) {
			Log.d(TAG, "No has SE Connection.");
			Util.makeLooperThread(new Runnable() {

				public void run() {
					l.onNoSE();
				}
			});
			return;
		}
		checkMobileId(l);
	}

	private void checkMobileId(final LauncherListener l) {
		// get mobile id
		// context.getSEManager().clearMobileId();
		// String mobileId = null;
		// try {
		// mobileId = context.getSEManager().getMobileId();
		// } catch (Exception e) {
		//
		// mobileId = null;
		// e.printStackTrace();
		// }
		// // context.setMobileId(mobileId);
		// Log.d(TAG, "checkMobileId>> mobileId :: " + mobileId);
		// check if lock status by checking server.
		DeviceInfo device = new DeviceInfo(context);
		String iccid = device.getICCID();
		// Log.d(TAG,"I");
		RequestCustomerIdbyMobileUIDRq cmsreq = new RequestCustomerIdbyMobileUIDRq(
				iccid, "ICCID");
		final RequestCustomerIdbyMobileUIDRs res;
		WalletGateWay gateway = context.getNetworkManager().getWalletGateWay();
		try {
			res = gateway.requestCustomerIdbyMobileUID(cmsreq);
			if (res != null && res.getWalletStatus() != null
					&& res.getResult().getCode() == Result.OK) {
				if (res.getWalletStatus().equals(
						RequestCustomerIdbyMobileUIDRs.ACTIVE_STATE)) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onCompleted();
						}
					});
					return;
				} else if (res.getWalletStatus().equals(
						RequestCustomerIdbyMobileUIDRs.LOCKED_STATE)) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onLocked();
						}
					});
					return;
				} else if (res.getWalletStatus().equals(
						RequestCustomerIdbyMobileUIDRs.NO_REGISTRATION)) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res.getCustomerId() != null) {
								l.onNeedActivate(res.getCustomerId(),
										res.getCustomerInfoExistYN());
							} else {
								l.onNeedActivate("",
										res.getCustomerInfoExistYN());
							}
						}
					});
					return;
				} else if (res.getWalletStatus().equals(
						RequestCustomerIdbyMobileUIDRs.PREACTIVE)) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res.getCustomerId() != null) {
								l.onNeedActivate(res.getCustomerId(),
										res.getCustomerInfoExistYN());
							} else {
								l.onNeedActivate("",
										res.getCustomerInfoExistYN());
							}
						}
					});
					return;
				}
			} else {
				Util.makeLooperThread(new Runnable() {

					@Override
					public void run() {
						if (res != null) {
							l.onFailed(res.getResult().getCode(), res
									.getResult().getMessage());
						}
					}
				});
				return;
			}
		} catch (NoNetworkException e) {
			Util.makeLooperThread(new Runnable() {

				public void run() {
					l.onNoNetwork();
				}
			});
			return;
		} catch (NoResponseException e) {
			Util.makeLooperThread(new Runnable() {

				public void run() {
					l.onNoResponse();
				}
			});
			return;
		} catch (final ResNotOKException e) {
			Util.makeLooperThread(new Runnable() {

				public void run() {
					l.onFailed(e.getCode(), e.getMessage());
				}
			});
			return;
		}
		Util.makeLooperThread(new Runnable() {

			public void run() {
				l.onCompleted();
			}
		});
		return;
	}

	/**
	 * Called when user selects cancel for optional wallet update
	 * 
	 * @param l
	 */
	public void onOptionalUpdateCanceled(final LauncherListener l) {
		new Thread(new Runnable() {

			public void run() {
				checkMobileId(l);
			}
		}).start();
	}

	/**
	 * Called after user is notified of uicc swap
	 * 
	 * @param l
	 */
	public void onUICCSwapped(final LauncherListener l) {
		context.getSettingManager().putString(WALLET_ICCID_KEY, "");
		start(l);
	}

	/**
	 * Called after user is notified of device change.
	 * 
	 * @param l
	 */
	public void onDeviceChanged(final LauncherListener l) {
		new Thread(new Runnable() {

			public void run() {
				String mobileId = null;
				try {
					mobileId = context.getSEManager().getMobileId();
				} catch (Exception e) {
					e.printStackTrace();
					mobileId = null;
				}
				if (TextUtils.isEmpty(mobileId) == false) {
					activateWalletClient(null, new ActivationListener() {

						@Override
						public void onSessionTimeOut() {
							if (l != null)
								l.onSessionTimeOut();
						}

						@Override
						public void onNoResponse() {
							if (l != null)
								l.onNoResponse();
						}

						@Override
						public void onNoNetwork() {
							if (l != null)
								l.onNoNetwork();
						}

						@Override
						public void onFinished() {
						}

						@Override
						public void onGettingStateFailed(int errorCode,
								String errorMsg) {
						}

						@Override
						public void onActivationNeeded() {
						}

						@Override
						public void onNoSe() {
						}
					});
				}
				// startInThread(l);
			}
		}).start();
	}

	protected void writeWalletPkg() {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// get the info from the currently running task
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		context.getSettingManager().putString(
				NetworkManager.WALLET_PACKAGE_NAME,
				componentInfo.getPackageName());
	}

	/**
	 * <pre>
	 * User want to set up wallet.
	 * This method will check network's status and request server to setup.
	 * After getting TnC, the {@link TncListener#onSuccess(String)} will be called.
	 * After accepting TnC, the {@link TncListener#onAcceptTnc()} will be called.
	 * The {@link TncListener#onAcceptTnc()} will request server to get a new activation PIN.
	 * If there is no available networks, {@link TncListener#onNetworkError()} will be called.
	 * </pre>
	 * 
	 * @param l
	 * @see #agreeTnC(TncListener)
	 */
	public void getWoAccountTNC(final TncListener l) {
		new Thread(new Runnable() {

			public void run() {
				// check network's status
				if (context.getNetworkManager().isNetworkAvailable() == false) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				}
				// get tnc
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				TnCRq req = new TnCRq(WALLETID, Locale.getDefault()
						.getLanguage());
				final TnCRs res;
				try {
					res = gateway.getWalletTnC(req);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess(res.getTnc());
							} else {
								if (res != null) {
									l.onFail(res.getResult().getMessage());
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * <pre>
	 * User want to set up wallet.
	 * This method will check network's status and request server to setup.
	 * After getting TnC, the {@link PostSetUpListener#onRcvTnC(String)} will be called.
	 * After accepting TnC, the {@link PostSetUpListener#onAcceptTnc()} will be called.
	 * The {@link PostSetUpListener#onAcceptTnc()} will request server to get a new activation PIN.
	 * If there is no available networks, {@link PostSetUpListener#onNetworkError()} will be called.
	 * </pre>
	 * 
	 * @param email
	 * @param l
	 * @see #getTnC(PreSetUpListener)
	 */
	/**
	 * Returns the application ID for this wallet.
	 * 
	 * @return
	 */
	public String getWalletAppId() {
		return appId;
	}

	/**
	 * Returns the application version
	 * 
	 * @return
	 */
	public String getWalletVersion() {
		return getAppVersion(context.getPackageName());
	}

	/**
	 * Return the version of the app having the package name
	 * 
	 * @param pkgName
	 *            package name
	 * @return null if not found package name.
	 */
	public String getAppVersion(String pkgName) {
		PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo;
		String versionCode = null;
		try {
			packageInfo = pm.getPackageInfo(pkgName, 0);
			versionCode = String.valueOf(packageInfo.versionName); // versionName->versionCode
		} catch (NameNotFoundException e) {
		}
		return versionCode;
	}

	/**
	 * Execute the SP application.
	 * 
	 * @param spApp
	 * @throws SpNotInstalledException
	 *             there is no SP application.
	 * @throws SpNotRegisteredException
	 *             there is no SP application and need to register.
	 * @see #showInstallDialog(DialogMessage, SpService)
	 * @see #showRegisterDialog(DialogMessage, SpService)
	 */
	public void executeApp(final SpService spApp, String activityName)
			throws SpNotInstalledException, SpNotRegisteredException {
		// executeApp
		executeApp(spApp, null, activityName);
	}

	/**
	 * Execute the SP application and deliver the parameter.
	 * 
	 * @param spApp
	 * @param param
	 * @throws SpNotInstalledException
	 *             there is no SP application.
	 * @throws SpNotRegisteredException
	 *             there is no SP application and need to register.
	 * @see #showInstallDialog(DialogMessage, SpService)
	 * @see #showRegisterDialog(DialogMessage, SpService)
	 */
	public void executeApp(final SpService spApp, final Bundle param,
			String activityName) throws SpNotInstalledException,
			SpNotRegisteredException {
		try {
			Intent intent = new Intent();
			ComponentName componentName = new ComponentName(
					spApp.getAppPackageName(), spApp.getAppPackageName()
							+ activityName);
			intent.setComponent(componentName);
			// intent.setAction(spApp.getAppPackageName() + ACTION_MAIN_POST);
			intent.putExtra("AppID", spApp.getServiceId());
			intent.putExtra("AID", spApp.getAppletAid());
			Log.d(TAG, "传递的AppID" + spApp.getServiceId());
			Log.d(TAG, "传递的AID" + spApp.getAppletAid());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			Log.d(TAG, "executeApp>> start SP activity");
		} catch (ActivityNotFoundException anfe) {
			throw new SpNotInstalledException();
		}
	}

	/**
	 * <pre>
	 * Show dialog message box for requesting to install.
	 * The dialog must have two buttons, OK and cancel. If user selects the OK,
	 * this will lead application to install the SP application.
	 * </pre>
	 * 
	 * @param dialog
	 * @param spApp
	 *            The spApp must have app ID and version.
	 * @see #executeApp(SpService)
	 * @see #executeApp(SpService, Bundle)
	 * @see #installOrUpdate(SpService, Runnable)
	 */
	public void showInstallDialog(final DialogMessage dialog,
			final SpService spApp) {
		final Runnable okAction = dialog.getActionForBtn(1); // if locale
																// change, the
																// index might
																// be affected
																// by it.
		DialogAction installOkAction = new DialogAction(okAction,
				new Runnable() {

					public void run() {
						boolean isSuccess = true;
						Log.d(TAG, "showInstallDialog>> check eligibility");
						SpAppGateWay gateway = context.getNetworkManager()
								.getSpAppGateWay();
						CheckEligibilityRq req1 = new CheckEligibilityRq(spApp
								.getServiceId());
						try {
							ResultRs res1 = gateway.checkEligibility(req1);
							if (res1 != null
									&& res1.getResult().getCode() == Result.OK) {
								Log.d(TAG,
										"showInstallDialog>> request install");
								InstallSpAppRq req = new InstallSpAppRq(spApp
										.getServiceId(), spApp
										.getServiceVersion());
								ResultRs res = gateway.installSpService(req);
								if (res == null
										|| res.getResult().getCode() != Result.OK) {
									isSuccess = false;
								}
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							isSuccess = false;
						}
						if (isSuccess == false) {
							Log.d(TAG,
									"showInstallDialog>> send broadcast to activity of fail of requesting install.");
							Intent broadcast = new Intent(
									WalletManager.SP_APP_INSTALL_ACTION);
							broadcast.putExtra(WalletManager.EXTRA_SP_APP_ID,
									spApp.getServiceId());
							broadcast.putExtra(
									WalletManager.EXTRA_SP_OP_RESULT, "fail");
							context.sendBroadcast(broadcast);
						}
					}
				});
		dialog.setActionForBtn(1, installOkAction);
		dialog.show();
	}

	/**
	 * <pre>
	 * Show dialog message box for requesting to register.
	 * The dialog must have two buttons, OK and cancel. If user selects the OK,
	 * this will lead application to register to the SP application.
	 * </pre>
	 * 
	 * @param dialog
	 * @param spApp
	 * @see #executeApp(SpService)
	 * @see #executeApp(SpService, Bundle)
	 */
	public void showRegisterDialog(final DialogMessage dialog,
			final SpService spApp) {
		dialog.show();
	}

	/**
	 * <pre>
	 * Show dialog message box for requesting to remove.
	 * The dialog must have two buttons, OK and cancel. If user selects the OK,
	 * this will lead application not only to remove the SP application,but to unregister.
	 * </pre>
	 * 
	 * @param dialog
	 * @param spApp
	 * @see #uninstall(SpService, Runnable)
	 */
	public void showRemoveDialog(final DialogMessage dialog,
			final SpService spApp) {
		final Runnable okAction = dialog.getActionForBtn(1); // if locale
																// change, the
																// index might
																// be affected
																// by it.
		DialogAction removeOkAction = new DialogAction(okAction,
				new Runnable() {

					public void run() {
						/** start activity to remove package **/
						try {
							spApp.setAppState(SpService.DELETING_STATUS); // mark
							context.getSpServiceManager().getDatabase()
									.updateAppState(spApp);
							// start activity for deleting
							context.getPackageManager().getPackageInfo(
									spApp.getAppPackageName(), 0);
							Uri uri = Uri.fromParts("package",
									spApp.getAppPackageName(), null);
							Intent it = new Intent(Intent.ACTION_DELETE, uri);
							Activity activity = dialog.getActivity();
							activity.startActivityForResult(it,
									ACTION_DELETE_REQUEST);
							Log.d(TAG,
									"showRemoveDialog>> start acitvity to un-install. ::"
											+ spApp.getServiceName());
							/**
							 * call checkDMResult after 15 seconds. Intent it2 =
							 * new
							 * Intent(PushConstant.PUSH_SP_APP_DELETION_ACTION);
							 * it2.putExtra(PushConstant.
							 * PUSH_NOTIFICATION_DATA_SERVICE,
							 * spApp.getServiceId()); PendingIntent pintent =
							 * PendingIntent.getService(context, 0, it2,
							 * PendingIntent.FLAG_ONE_SHOT ); AlarmManager am =
							 * (AlarmManager)context.getSystemService(Activity.
							 * ALARM_SERVICE); Calendar cal =
							 * Calendar.getInstance(); cal.add(Calendar.SECOND,
							 * 15); am.set(AlarmManager.RTC_WAKEUP,
							 * cal.getTimeInMillis(), pintent); Log.d(TAG,
							 * "showRemoveDialog>> start service to check un-install after 15 seconds."
							 * );
							 */
						} catch (NameNotFoundException e) {
						}
					}
				});
		dialog.setActionForBtn(1, removeOkAction);
		dialog.show();
	}

	/**
	 * Request for downloading and installing(updating) SP application.
	 * 
	 * @param activity
	 * @param downUrl
	 * @param serviceId
	 * @throws DiskNotReadyException
	 */
	public void requestInstallUpdateApp(final Activity activity,
			final String downUrl, final String serviceId)
			throws DiskNotReadyException {
		if (DMService.isWritableExternalDisk() == false)
			throw new DiskNotReadyException();
		String action = SP_APP_INSTALL_ACTION;
		// SpService svc = context.getSpServiceManager().getDatabase()
		// .getSpAppById(serviceId);
		// if (svc != null && svc.getAppState() == SpService.INSTALLED_STATUS) {
		// action = SP_APP_UPDATE_ACTION;
		// }
		DMService dmSvc = new DMService(activity, downUrl, serviceId, action);
		dmSvc.start();
	}

	/**
	 * Roll-back delete SP service request.
	 * 
	 * @param spApp
	 */
	public void rollbackDeleteReq(final SpService spApp) {
		if (context.getSpServiceManager().isInstalledOnDevice(
				spApp.getAppPackageName())) {
			spApp.setAppState(SpService.INSTALLED_STATUS);
			context.getSpServiceManager().getDatabase().updateAppState(spApp);
		}
	}

	/**
	 * Reset.
	 * 
	 * @param l
	 */
	public void reset(final ResetListener l) {
		new Thread(new Runnable() {

			public void run() {
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				final ResultRs res;
				try {
					res = gateway.resetWallet();
					if (res == null || res.getResult().getCode() != Result.OK) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onFail(res.getResult().getMessage());
							}
						});
					} else {
						context.getSettingManager().putInt(WALLET_STATUS_KEY,
								SpService.DELETING_STATUS);
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onReset();
							}
						});
					}
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Called when all data from server and SE are cleared.
	 */
	public void resetCompleted() {
		context.getSEManager().clearMobileId();
		context.getSettingManager().clear();
		context.getSpServiceManager().getDatabase().removeAll();
		// phase1 no use push
		// context.getPushNoticeManager().getDatabase().removeAll();
		context.getInBoxManager().getDatabase().removeAll();
		context.getNetworkManager().setCookie(null);
		Log.d(TAG, "resetCompleted>> all data are cleared.");
	}

	/**
	 * The try count of verifyActivationCode
	 */
	// private int countOfVerification = 0;
	/**
	 * <pre>
	 * Verify activation code.
	 * If trial fails over 3 times,terminated
	 * </pre>
	 * 
	 * @param code
	 * @param l
	 */
	public void verifyActivationCode(final String code,
			final ActivationListener l) {
		new Thread(new Runnable() {

			public void run() {
				activateWalletClient(code, l);
			}
		}).start();
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}

	/**
	 * @return
	 */
	public String getWelcomeMsg() {
		boolean isRead = context.getSettingManager().getBoolean(
				WELCOME_MSG_READ_KEY, false);
		if (isRead)
			return null;
		context.getSettingManager().putBoolean(WELCOME_MSG_READ_KEY, true);
		return context.getSettingManager().getString(WELCOME_MSG_KEY, "");
	}

	private final static String WELCOME_MSG_KEY = "WELCOME_MSG_KEY";
	private final static String WELCOME_MSG_READ_KEY = "WELCOME_MSG_READ_KEY";

	/**
	 * @param welcomeMsg
	 */
	public void setWelcomeMsg(String welcomeMsg) {
		String msg = context.getSettingManager().getString(WELCOME_MSG_KEY, "");
		if (TextUtils.isEmpty(msg)) {
			context.getSettingManager().putString(WELCOME_MSG_KEY, welcomeMsg);
			context.getSettingManager().putBoolean(WELCOME_MSG_READ_KEY, false);
		}
	}

	static class DialogAction implements Runnable {

		Runnable uiAction;
		Runnable action;

		DialogAction(Runnable uiAction, Runnable action) {
			this.uiAction = uiAction;
			this.action = action;
		}

		@Override
		public void run() {
			new Thread(new Runnable() {

				public void run() {
					action.run();
					if (uiAction != null) {
						Util.makeLooperThread(uiAction);
					}
				}
			}).start();
		}
	}

	public void resetPassword(final String newPwd, final ResetPasswordListener l) {
		new Thread(new Runnable() {

			public void run() {
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				ResetWoPasswordRq req = new ResetWoPasswordRq();
				String woAccountId = context.getWoAccountId();
				req.setWoAccountId(woAccountId); // : 나중에
													// context.getSettingManager().getString
				DeviceInfo deviceInfo = new DeviceInfo(context); // 에서 가져오도록 수정할
																	// 것.
				req.setMobileUidType("ICCID");
				req.setMobileUniqueId(deviceInfo.getICCID());
				req.setLoginPwd(newPwd);
				try {
					final ResetWoPasswordRs res = gateway.resetPassword(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoResponse();
						}
					});
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param oldPw
	 * @param newPw
	 * @param l
	 * @author jinxing
	 */
	public void changePassword(final String oldPw, final String newPw,
			final ChangePasswordListener l) {
		new Thread(new Runnable() {

			public void run() {
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				String mobileId = null;
				try {
					mobileId = context.getSEManager().getMobileId();
				} catch (Exception e1) {
					e1.printStackTrace();
					mobileId = null;
				}
				String woAccountId = context.getWoAccountId();
				ChangePasswordRq req = new ChangePasswordRq();
				req.setOldPsw(oldPw);
				req.setNewPsw(newPw);
				req.setMobileId(mobileId);
				req.setWoAccountId(woAccountId);
				Log.d(TAG, "changePassword--oldPassword**" + oldPw
						+ "newPassword**" + newPw + "mobileId///" + mobileId
						+ "woAccountId" + woAccountId);
				try {
					final ChangePasswordRs res = gateway.changePassword(req);
					if (res != null) {
						Log.d(TAG, "changePassword__res:CODE::::"
								+ res.getResult().getCode() + "res:MSG:::"
								+ res.getResult().getMessage());
					} else {
						Log.d(TAG, "changePassword_res==" + res);
					}
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							} else {
								if (res != null) {
									l.onFail(res.getResult().getCode(), res
											.getResult().getMessage());
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoResponse();
						}
					});
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	// : 전문 Format 에 따라서 구현할 것.
	public void findPassword(final String userId, final FindPasswordListener l) {
		new Thread(new Runnable() {

			public void run() {
				// check network's status
				if (context.getNetworkManager().isNetworkAvailable() == false) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				}
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				VerifyUserIdRq req = new VerifyUserIdRq();
				String woAccountId = context.getWoAccountId();
				req.setWoAccountId(woAccountId); // : 메모리에 저장된 값을 읽어올 것.
				DeviceInfo device = new DeviceInfo(context);
				String iccid = device.getICCID();
				req.setUserId(userId);
				req.setMobileUniqueId(iccid);
				req.setMobileUidType("ICCID");
				try {
					final ResultRs res = gateway.verifyUserId(req);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							}
						}
					});
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param
	 * @param l
	 */
	public void checkWoAccountId(final String userid,
			final CheckWoAccountIdListener l) {
		new Thread(new Runnable() {

			public void run() {
				// check network's status
				if (context.getNetworkManager().isNetworkAvailable() == false) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoNetwork();
						}
					});
					return;
				}
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				CheckWoAccountIdRq req = new CheckWoAccountIdRq();
				DeviceInfo device = new DeviceInfo(context);
				String iccid = device.getICCID();
				req.setMobileUniqueId(iccid);
				req.setMobileUidType("ICCID");
				req.setWoAccountId(userid);
				try {
					final CheckWoAccountIdRs res = gateway
							.checkWoAccountId(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								// 将woAccountID记录下来，登陆和激活时也会记录
								context.setWoAccountId(userid);
								l.onSuccess();
							}
						}
					});
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoNetwork();
						}
					});
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoResponse();
						}
					});
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	// 구현할 것.
	// 로그인 성공 시 전문을 통해서 정보를 받은 후 메모리(WApplication 에 set, get 등록)에 저장하고
	// 로그아웃 시 삭제한다.
	public Map<String, String> getUserBasicInfo(String iccid) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("Name", context.getName());
		hashMap.put("IDType", context.getIdType());
		hashMap.put("IDValue", context.getIdValue());
		hashMap.put("PhoneNO", context.getPhoneNo());
		return hashMap;
	}

	/**
	 * 去哪儿网调用主客户端写卡时，将写卡信息缓存在此
	 * */
	public HashMap<String, String> getEticketData() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put(ETICKET_AID, context.getEticket_aid());
		hashMap.put(ETICKET_SERVICEID, context.getEticket_serviceId());
		hashMap.put(ETICKET_APDU, context.getEticket_apdu());
		hashMap.put(ETICKET_SUCCESSMSG, context.getEticket_successMsg());
		hashMap.put(ETICKET_FAILMSG, context.getEticket_failMsg());
		hashMap.put(ETICKET_ORDERID, context.getEticket_orderId());
		return hashMap;
	}

	/**
	 * @author skcc
	 */
	public void requestActivationCode(final String msisdn, final SmsListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				DeviceInfo device = new DeviceInfo(context);
				RequestActivationCodeRq req = new RequestActivationCodeRq();
				req.setMsisdn(msisdn);
				req.setMobileUniqueId(device.getICCID());
				req.setMobileUidType("ICCID");
				try {
					final RequestActivationCodeRs res = gateway
							.requestActivationCode(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							} else {
								if (res != null) {
									l.onFail(res.getResult().getMessage());
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoNetwork();
						}
					});
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							l.onNoResponse();
						}
					});
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * activateWoAccount
	 * 
	 * @jinxing
	 */
	public void activateWoAccount(final String woAccountId,
			final String loginPwd, final String customerInfoExistYN,
			final String payPwd, final WoAccountActivationListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ActivateWoAccountRq req = new ActivateWoAccountRq();
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				// String mobileId;
				// try {
				// mobileId = context.getSEManager().getMobileId();
				// } catch (Exception e1) {
				// e1.printStackTrace();
				// mobileId = null;
				// }
				req.setWoAccountId(woAccountId);
				req.setLoginPwd(loginPwd);
				req.setCustomerInfoExistYN(customerInfoExistYN);
				DeviceInfo device = new DeviceInfo(context);
				req.setWalletId(WALLETID);
				req.setMobileUniqueId(device.getICCID());
				req.setMobileUidType("ICCID");
				req.setModelName(device.getDeviceModelName());
				req.setImei(device.getIMEI());
				req.setImsi(device.getIMSI());
				req.setPayPwd(payPwd);
				try {
					final ActivateWoAccountRs res = gateway
							.activateWoAccount(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								context.setWoAccountId(woAccountId);// 保存帐号,给后面用。
								l.onSuccess();
							} else {
								if (res != null) {
									l.onFail(res.getResult().getCode(), res
											.getResult().getMessage());
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	public void logoutWalletClient(final LogoutWalletClietnListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				LogoutWalletClientRq req = new LogoutWalletClientRq();
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				LogoutWalletClientRs res;
				try {
					res = gateway.logoutWalletClient(req);
					if (res.getResult().getCode() == 0) {
						l.onLogoutSucess();
					} else {
						l.onLogoutFail();
					}
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (ResNotOKException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * activateWalletClient
	 * 
	 * @param code
	 * @param l
	 */
	public void activateWalletClient(final String code,
			final WalletActivationListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ActivateWalletClientRq req = new ActivateWalletClientRq();
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				DeviceInfo device = new DeviceInfo(context);
				String mobileId = null;
				try {
					mobileId = context.getSEManager().getMobileId();
				} catch (Exception e1) {
					e1.printStackTrace();
					mobileId = null;
				}
				req.setMobileId(mobileId);
				req.setWalletId(WALLETID);
				req.setMobileUniqueId(device.getICCID());
				req.setMobileUidType("ICCID");
				req.setModelName(device.getDeviceModelName());
				req.setImei(device.getIMEI());
				req.setActivationCode(code);
				Log.d(TAG, "verifyActivationCode>> mobileId :: " + mobileId);
				try {
					final ActivateWalletClientRs res = gateway
							.activateWalletClient(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							} else {
								if (res != null) {
									l.onFail(res.getResult().getCode(), res
											.getResult().getMessage());
								}
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
						return;
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
						return;
					}
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onFail(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * activateWalletClient
	 * 
	 * @param code
	 * @param l
	 */
	public void activateWalletClient(final String code,
			final ActivationListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// mobileid walletid uniqueid uniquetype modelname imei
				// activatecode
				ActivateWalletClientRq req = new ActivateWalletClientRq();
				String mobileId = null;
				try {
					mobileId = context.getSEManager().getMobileId();
				} catch (Exception e1) {
					e1.printStackTrace();
					mobileId = null;
				}
				DeviceInfo device = new DeviceInfo(context);
				String iccid = device.getICCID();
				if (mobileId != null) {
					req.setMobileId(mobileId);
				}
				req.setWalletId(WALLETID);
				req.setMobileUniqueId(iccid);
				req.setMobileUidType("ICCID");
				req.setModelName(device.getDeviceModelName());
				req.setImei(device.getIMEI());
				if (code != null)
					req.setActivationCode(code);
				Log.d(TAG, "verifyActivationCode>> mobileId :: " + mobileId);
				WalletGateWay gateway = context.getNetworkManager()
						.getWalletGateWay();
				try {
					final ActivateWalletClientRs res = gateway
							.activateWalletClient(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res.getResult().getCode() == 0) {
								l.onFinished();
							} else {
								l.onActivationNeeded();
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (ResNotOKException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Send feedback to server
	 * */
	public void sendFeedback(final String content, final SendFeebBackListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SendFeedbackRq req = new SendFeedbackRq();
				DeviceInfo deviceInfo = new DeviceInfo(context);
				req.setAppVersionName(getWalletVersion());
				req.setWalletId(WALLETID);
				req.setModelName(deviceInfo.getDeviceModelName());
				req.setCustomerId(context.getWoAccountId());
				req.setFeedbackContent(content);
				WalletGateWay walletGateWay = context.getNetworkManager()
						.getWalletGateWay();
				try {
					final SendFeedbackRs res = walletGateWay.senFeedback(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onSuccess();
							} else {
								l.onFail();
							}
						}
					});
				} catch (NoNetworkException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (ResNotOKException e) {
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onFail();
							}
						});
					}
				}
			}
		}).start();
	}

	public void getVersionHistory(final int pageNumber, final int pageSize,
			final String walletId, final GetWalletVersionHistoryListener l) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestWalletVersionHistRq req = new RequestWalletVersionHistRq(
						pageNumber, pageSize);
				req.setWalletId(walletId);
				WalletGateWay walletGateWay = context.getNetworkManager()
						.getWalletGateWay();
				try {
					final RequestWalletVersionHistRs res = walletGateWay
							.getWalletVersionHistory(req);
					Util.makeLooperThread(new Runnable() {

						@Override
						public void run() {
							if (res != null
									&& res.getResult().getCode() == Result.OK) {
								l.onResult(res.getDeviceAppVersionList(),
										res.getPageInfo());
							}
						}
					});

				} catch (NoNetworkException e) {
					e.printStackTrace();
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							public void run() {
								l.onNoNetwork();
							}
						});
					}
					return;
				} catch (NoResponseException e) {
					e.printStackTrace();
					if (l != null) {
						Util.makeLooperThread(new Runnable() {
							public void run() {
								l.onNoResponse();
							}
						});
					}
					return;
				} catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}
				// Util.makeLooperThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// if (res != null
				// && res.getResult().getCode() == Result.OK) {
				// l.onSuccess();
				// } else {
				// l.onFail();
				// }
				// }
				// });
				// } catch (NoNetworkException e) {
				// if (l != null) {
				// Util.makeLooperThread(new Runnable() {
				//
				// public void run() {
				// l.onNoNetwork();
				// }
				// });
				// }
				// } catch (NoResponseException e) {
				// if (l != null) {
				// Util.makeLooperThread(new Runnable() {
				//
				// public void run() {
				// l.onNoResponse();
				// }
				// });
				// }
				// } catch (ResNotOKException e) {
				// if (l != null) {
				// Util.makeLooperThread(new Runnable() {
				//
				// public void run() {
				// l.onFail();
				// }
				// });
				// }
				// }
				// }
			}
		}).start();
	}

	public boolean checkSPDeviceAppSignature(final String serviceId,
			final String signature) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				CheckSPDeviceAppSignatureRq rq = new CheckSPDeviceAppSignatureRq();
				DeviceInfo deviceInfo = new DeviceInfo(context);
				rq.setMobileUniqueId(deviceInfo.getICCID());
				rq.setMobileUidType("ICCID");
				rq.setServiceId(serviceId);
				rq.setSignature(signature);
				WalletGateWay walletGateWay = context.getNetworkManager()
						.getWalletGateWay();
				try {
					CheckSPDeviceAppSignatureRs res = walletGateWay
							.checkSPDeviceAppSignature(rq);
					if (res != null && res.getResult().getCode() == Result.OK) {
						setResult(true);
						notifyMyThread();
					} else {
						notifyMyThread();
					}
				} catch (NoNetworkException e) {
					setResult(false);
					notifyMyThread();
					e.printStackTrace();
				} catch (NoResponseException e) {
					setResult(false);
					notifyMyThread();
					e.printStackTrace();
				} catch (ResNotOKException e) {
					setResult(false);
					notifyMyThread();
					e.printStackTrace();
				}
			}
		}).start();
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				setResult(false);
				e.printStackTrace();
			}
		}

		return checkPermissionResult;
	}

	private boolean checkPermissionResult = false;
	private final Object lock = new Object();

	private void notifyMyThread() {
		synchronized (lock) {
			lock.notify();
		}
	}

	private void setResult(boolean flag) {
		checkPermissionResult = flag;
	}

	private void recordCustomerSpOrder(final String writeResult) {
		context.getSpServiceManager().recordCustomerSpOrder(
				context.getWoAccountId(),
				getEticketData().get(ETICKET_SERVICEID),
				getEticketData().get(ETICKET_ORDERID), writeResult,
				new RecordCustomerSpOrderListener() {

					@Override
					public void onSessionTimeOut() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNoResponse() {
						// TODO Auto-generated method stub
						Log.d(TAG, "recordFailed>onNoResponse>>>>>>>");
					}

					@Override
					public void onNoNetwork() {
						// TODO Auto-generated method stub
						Log.d(TAG, "recordFailed>onNoNetwork>>>>>>>");
					}

					@Override
					public void recordSuccess() {
						// TODO Auto-generated method stub
						Log.d(TAG, "recordSuccess>>>>>>>>>");
					}

					@Override
					public void recordFailed() {
						// TODO Auto-generated method stub
						Log.d(TAG, "recordFailed>>>>>>>>>");

					}
				});

	}
}

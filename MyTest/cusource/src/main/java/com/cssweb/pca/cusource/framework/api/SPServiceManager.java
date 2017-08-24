package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.api.http.SpAppGateWay;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateAppRq;
import cn.unicompay.wallet.client.framework.api.http.model.CheckUpdateAppRs;
import cn.unicompay.wallet.client.framework.api.http.model.Condition;
import cn.unicompay.wallet.client.framework.api.http.model.GetGuiAppListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetGuiAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.GetMyAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.PageInfo;
import cn.unicompay.wallet.client.framework.api.http.model.RecordSpOrderRq;
import cn.unicompay.wallet.client.framework.api.http.model.RecordSpOrderRs;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveAuthFailedRq;
import cn.unicompay.wallet.client.framework.api.http.model.RemoveAuthFailedRs;
import cn.unicompay.wallet.client.framework.api.http.model.Result;
import cn.unicompay.wallet.client.framework.api.http.model.SearchAppListRq;
import cn.unicompay.wallet.client.framework.api.http.model.SearchAppListRs;
import cn.unicompay.wallet.client.framework.api.http.model.ViewAppInfoRq;
import cn.unicompay.wallet.client.framework.api.http.model.ViewAppInfoRs;
import cn.unicompay.wallet.client.framework.db.MySpServiceDB;
import cn.unicompay.wallet.client.framework.model.Category;
import cn.unicompay.wallet.client.framework.model.SP;
import cn.unicompay.wallet.client.framework.model.SpService;
import cn.unicompay.wallet.client.framework.util.Util;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;
import com.skcc.wallet.core.se.SException;
import com.skcc.wallet.core.se.instance.CRSApplication;

public class SPServiceManager {

	private static final String TAG = "SPServiceManager";
	private final static String NONETWORKEXCEPTION = "NoNetworkException";
	private final static String NORESPONSEEXCEPTION = "NoResponseException";
	/** Context **/
	private WApplication context;
	/** The database **/
	private MySpServiceDB database;
	/**
	 * Category information
	 */
	private Vector<Category> categories;
	/**
	 * SP list
	 */
	private Vector<SP> spList;

	/**
	 * <pre>
	 * Constructor 
	 * Create database object and get categories.
	 * </pre>
	 * 
	 * @param context
	 */
	public SPServiceManager(WApplication context) {
		this.context = context;
		database = new MySpServiceDB(context);
		categories = database.getCategory();
		spList = database.getSP();
	}

	/**
	 * Return the SP application list.
	 * 
	 * @param categoryId
	 * @param sortBy
	 * @return if no SP applications, return empty vector
	 * @see MySpServiceDB#getSpAppList(String, byte)
	 * @see MySpServiceDB#SEARCH_BY_ID
	 * @see MySpServiceDB#SEARCH_BY_PACKAGE_NAME
	 * @see MySpServiceDB#SEARCH_BY_UID
	 * @see MySpServiceDB#CATEGORY_ALL_ID
	 */
	public Vector<SpService> getMySpAppList(String categoryId, byte sortBy) {
		return database.getSpAppList(categoryId, sortBy);
	}

	/**
	 * Return the SP application from searching by.
	 * 
	 * @param keyword
	 * @param searchBy
	 * @return null if not found
	 * @see MySpServiceDB#SEARCH_BY_ID
	 * @see MySpServiceDB#SEARCH_BY_PACKAGE_NAME
	 * @see MySpServiceDB#SEARCH_BY_UID
	 */
	public SpService searchMySpApp(String keyword, int searchBy) {
		if (keyword == null)
			return null;
		if (searchBy == (MySpServiceDB.SEARCH_BY_ID)) {
			return database.getSpAppById(keyword);
		} else if (searchBy == (MySpServiceDB.SEARCH_BY_PACKAGE_NAME)) {
			return database.getSpAppByPackage(keyword);
		} else if (searchBy == (MySpServiceDB.SEARCH_BY_UID)) {
			return database.getSpAppByUid(keyword);
		}
		return null;
	}

	/**
	 * Returns the SP application with highest priority.
	 * 
	 * @return
	 */
	public final SpService getMainApp() {
		SettingManager setting = context.getSettingManager();
		String appId = setting.getString(MAIN_APP_KEY, "");
		Log.d(TAG, "getMainApp>> main service Id ::" + appId);
		if (TextUtils.isEmpty(appId))
			return null;
		return database.getSpAppById(appId);
	}

	/** */
	public static final String MAIN_APP_KEY = "MAIN_APP_KEY";

	/**
	 * Returns the application with temporary highest priority.
	 * 
	 * @return
	 * @see #getOneTimePaymentAID()
	 */
	public SpService getOneTimePayment() {
		SettingManager setting = context.getSettingManager();
		String appId = setting.getString(VOLATILE_APP_KEY, "");
		Log.d(TAG, "getOneTimePayment>> one time payment service Id :: "
				+ appId);
		if (TextUtils.isEmpty(appId))
			return null;
		return database.getSpAppById(appId);
	}

	/** */
	public static final String VOLATILE_APP_KEY = "VOLATILE_APP_KEY";
	public static final String VOLATILE_APP_PRIORITY = "VOLATILE_APP_PRIORITY";

	/**
	 * Returns the AID of the application with temporary highest priority.
	 * 
	 * @return
	 * @see #getOneTimePayment()
	 */
	public String getOneTimePaymentAID() {
		// getOneTimePaymentAID
		SpService app = getOneTimePayment();
		if (app == null)
			return null;
		return app.getAppletAid();
	}

	/**
	 * @return true if contactless interface on
	 */
	public boolean getContactlessStatus() {
		SettingManager setting = context.getSettingManager();
		return setting.getBoolean(CRSManager.CONTACTLESS_STATUS_KEY, false);
	}

	/**
	 * Refresh application's status by comparing it with device's status.
	 */
	public void refreshSpAppStatus() {
		// refreshSp App Status
		Vector<SpService> spAppList = getMySpAppList(
				MySpServiceDB.CATEGORY_ALL_ID, (byte) 0);
		String packageName;
		short appState;
		for (SpService app : spAppList) {
			packageName = app.getAppPackageName();
			appState = app.getAppState();
			Log.d(TAG, "refreshSpAppStatus>> packageName :: " + packageName
					+ ", appState::" + appState);
			if (appState == SpService.INSTALLED_STATUS) {
				if (isInstalledOnDevice(packageName) == false) {
					app.setAppState(SpService.NOT_INSTALLED_STATUS);
					database.updateAppState(app);
				}
			} else if (appState == SpService.NOT_INSTALLED_STATUS) {
				if (isInstalledOnDevice(packageName)) {
					app.setAppState(SpService.INSTALLED_STATUS);
					database.updateAppState(app);
				}
			}
		}
	}

	/**
	 * Refresh applet's status by comparing it with SE's status.
	 * 
	 * @throws SException
	 */
	public void refreshSpAppletStatus() throws SException {
		// refresh Sp Applet Status
		Vector<SpService> spAppList = getMySpAppList(
				MySpServiceDB.CATEGORY_ALL_ID, (byte) 0);
		CRSManager crsMgr = context.getSEManager().getCrsManager();
		CRSApplication.Application[] apps = crsMgr.getApps(true);
		String aid;
		boolean isExist;
		SettingManager setting = context.getSettingManager();
		setting.remove(MAIN_APP_KEY);
		for (SpService app : spAppList) {
			// if (app.getAppletState() != SpService.INSTALLED_STATUS)
			if (app.getServiceSubscriptionState() != SpService.INSTALLED_STATUS)
				continue;
			aid = app.getAppletAid();
			isExist = false;
			if (TextUtils.isEmpty(aid) == false) { // have aid.
				for (CRSApplication.Application applet : apps) {
					Log.d(TAG,
							"refreshSpAppletStatus>> serviceName :: "
									+ app.getServiceName() + ", aid1::" + aid
									+ ", aid2::" + applet.aid);
					if (applet.aid.equals(aid)) {
						app.setPriority((short) applet.priority);
						database.updateSpPriority(app);
						if (app.getServiceCategoryId().equalsIgnoreCase(
								"Payments")) {
							// app.setAppletState(SpService.INSTALLED_STATUS);
							app.setServiceSubscriptionState(SpService.INSTALLED_STATUS);
							if (applet.status == CRSApplication.Application.STATUS_ACTIVATED) {
								app.setPriority(SpService.MAIN_CARD_PRIORITY);
								database.updateSpPriority(app);
								setting.putString(MAIN_APP_KEY,
										app.getServiceId());
							}
						} else {
							if (applet.status == CRSApplication.Application.STATUS_DEACTIVATED) {
								// app.setAppletState(SpService.LOCKED_STATUS);
								Log.d(TAG, "非支付卡设置LOCKED_STATUS>>>>>");
								app.setServiceSubscriptionState(SpService.LOCKED_STATUS);
							} else if (applet.status == CRSApplication.Application.STATUS_ACTIVATED) {
								// app.setAppletState(SpService.INSTALLED_STATUS);
								Log.d(TAG, "非支付卡设置INSTALLED_STATUS>>>>>");
								app.setServiceSubscriptionState(SpService.INSTALLED_STATUS);
							}
						}
						database.updateAppletState(app);
						isExist = true;
						break;
					}
				}
				if (isExist == false) {
					// if (app.getAppletState() == SpService.INSTALLED_STATUS) {
					// app.setAppletState(SpService.NOT_INSTALLED_STATUS);
					// }
					// 是不是应该 INSTALLED_Ready_STATUS
					// database.updateAppletState(app);
				}
			}
		}
		// save main card and volatile(single payment) apps.
		// SettingManager setting = context.getSettingManager();
		// String oneTimeServiceId = setting.getString(VOLATILE_APP_KEY, "");
		// Log.d(TAG, "refreshSpAppletStatus>> one time payment service id :: "
		// + oneTimeServiceId);
		// String mainCardServiceId = setting.getString(MAIN_APP_KEY, "");
		// Log.d(TAG, "refreshSpAppletStatus>> main card service id :: "
		// + mainCardServiceId);
		// spAppList = getMySpAppList(MySpServiceDB.CATEGORY_ALL_ID, (byte) 0);
		// boolean hasMain = false;
		// boolean hasVolatile = false;
		// for (SpService app : spAppList) {
		// if (TextUtils.isEmpty(app.getServiceId()) == false
		// && app.getServiceCategoryId().equals("Payments")) {
		// if (oneTimeServiceId.equals(app.getServiceId())
		// && app.getAppletState() == SpService.INSTALLED_STATUS) {
		// setting.putString(VOLATILE_APP_KEY, app.getServiceId());
		// setting.putInt(VOLATILE_APP_PRIORITY, app.getPriority());
		// hasVolatile = true;
		// continue;
		// }
		// if (mainCardServiceId.equals(app.getServiceId())) {
		// setting.putString(MAIN_APP_KEY, app.getServiceId());
		// hasMain = true;
		// continue;
		// }
		// // if (hasMain == false) {
		// // // setting.putString(MAIN_APP_KEY, app.getServiceId());
		// // hasMain = true;
		// // }
		// }
		// }
		// if (hasVolatile == false) {
		// setting.remove(VOLATILE_APP_KEY);
		// }
	}

	/**
	 * delete Applet, refresh Status
	 * 
	 */
	public void deleteSpservice(String serviceId) {
		int i = remove(serviceId);
		SettingManager setting = context.getSettingManager();
		if (getMainApp() != null
				&& getMainApp().getServiceId().equals(serviceId)) {
			setting.remove(MAIN_APP_KEY);
		}
		if (getOneTimePayment() != null
				&& getOneTimePayment().equals(serviceId)) {
			setting.remove(VOLATILE_APP_KEY);
		}
		Log.d(TAG, "deleteCount>>>>>>>>" + i);
	}

	/**
	 * Return the row ID from inserting a new SP application.
	 * 
	 * @param app
	 * @return
	 * @see MySpServiceDB#insert(SpService)
	 */
	long insert(SpService app) {
		// 一期是以jar包形式，没有必要比较已经安装的应用
		// check package info and set status..
		/*
		 * String version = context.getWalletManager().getAppVersion(
		 * app.getAppPackageName()); if (version == null) {
		 * app.setAppState(SpService.NOT_INSTALLED_STATUS); } else {
		 * app.setAppVersion(version); }
		 */
		Log.d(TAG, "insert>> service name :: " + app.getServiceName()
				+ ", version :: " + app.getAppVersion());
		String aid = app.getAppletAid();
		if (TextUtils.isEmpty(aid) == false) {
			// check app
			long i = database.insert(app);
			Log.d(TAG, "insert>>>>>>>>>>>>>>>" + i);
			return i;
		}
		return -1;
	}

	/**
	 * Return the count of row affected by removing the SP application.
	 * 
	 * @param app
	 * @return
	 * @see MySpServiceDB#remove(SpService)
	 */
	int remove(SpService app) {
		return database.remove(app);
	}

	/**
	 * Return the count of row affected by removing the SP application by the
	 * application ID.
	 * 
	 * @param serviceId
	 * @return
	 * @see MySpServiceDB#remove(String)
	 */
	public int remove(String serviceId) {
		return database.remove(serviceId);
	}

	/**
	 * Return the count of row affected by removing all the SP applications.
	 * 
	 * @return
	 * @see MySpServiceDB#removeAll()
	 */
	public int removeAll() {
		return database.removeAll();
	}

	/**
	 * Return the count of row affected by updating the SP application.
	 * 
	 * @param app
	 * @return
	 * @see MySpServiceDB#update(SpService)
	 */
	int update(SpService app) {
		return database.update(app);
	}

	/**
	 * Increase the use count of the SP application.
	 * 
	 * @param serviceId
	 * @return
	 * @see MySpServiceDB#increaseUsgCnt(String)
	 */
	int increaseUseCnt(String serviceId) {
		return database.increaseUsgCnt(serviceId);
	}

	/**
	 * <pre>
	 * Get the current category list.
	 * </pre>
	 * 
	 * @return
	 * @see Category
	 */
	public Vector<Category> getSPCategory() {
		return categories;
	}

	/**
	 * Update database and set categories.
	 */
	void setSPCategory(Vector<Category> categories) {
		boolean hasFound = false;
		for (Category c1 : categories) {
			hasFound = false;
			for (Category c2 : this.categories) {
				if (c1.getCategoryId().equals(c2.getCategoryId())) {
					hasFound = true;
					if (c1.getCategoryName().equals(c2.getCategoryName()) == false) {
						database.updateCaegory(c1);
						Log.d(TAG,
								"setSPCategory>> category:"
										+ c1.getCategoryName());
					}
					break;
				}
			}
			if (hasFound == false) {
				long rowNum = database.insertCategory(c1);
				Log.d(TAG,
						"setSPCategory>> category :: " + c1.getCategoryName()
								+ ",rownum :: " + rowNum);
			}
		}
		this.categories = database.getCategory();
	}

	/**
	 * Get the current SP list.
	 * 
	 * @return
	 */
	public Vector<SP> getSPList() {
		return spList;
	}

	public int updateAppletState(SpService spService) {
		return database.updateAppletState(spService);
	}

	/**
	 * Update database and set spList.
	 * 
	 * @param spList
	 */
	void setSPList(Vector<SP> spList) {
		boolean hasFound = false;
		for (SP sp1 : spList) {
			hasFound = false;
			for (SP sp2 : this.spList) {
				if (sp1.getSpId().equals(sp2.getSpId())) {
					hasFound = true;
					if (sp1.getSpName().equals(sp2.getSpName()) == false) {
						database.updateSP(sp1);
					}
					break;
				}
			}
			if (hasFound == false) {
				long rowNum = database.insertSP(sp1);
				Log.d(TAG, "setSPList>> SP name :: " + sp1.getSpName()
						+ ",rownum :: " + rowNum);
			}
		}
		this.spList = database.getSP();
	}

	/**
	 * Get available SP applications
	 * 
	 * @param categoryId
	 * @param l
	 */
	public void getSpAppListOnStoreByCategory(final String categoryId,
			final AppStoreListener l) {
		new Thread(new Runnable() {

			public void run() {
				SearchAppListRq req = new SearchAppListRq();
				req.setPageInfo(new PageInfo(1, 100));
				if (categoryId != null
						&& categoryId.equals(MySpServiceDB.CATEGORY_ALL_ID) == false) {
					Vector<Condition> conditions = new Vector<Condition>();
					Condition c = new Condition();
					c.setType(Condition.TYPE_CATEGORY);
					c.setValue(categoryId);
					conditions.add(c);
					req.setConditionList(conditions);
				}
				SpAppGateWay gateway = context.getNetworkManager()
						.getSpAppGateWay();
				try {
					final SearchAppListRs res = gateway.searchServiceList(req);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {
									Vector<SpService> r = res.getServiceList();
									l.onList(r == null ? new Vector<SpService>(
											0) : r);
								} else {
									Log.d(TAG, "可加载列表Res>>>>>>>>>>>>" + res);
									if (res != null && l != null) {
										l.onError(res.getResult().getCode(),
												res.getResult().getMessage());
									}
								}
							}
						});
					}
				} catch (NoNetworkException e) {
					e.printStackTrace();
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoNetwork();
							}
						});
					}
				} catch (NoResponseException e) {
					e.printStackTrace();

					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.onNoResponse();
							}
						});
					}
				} catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Search SP applications with name
	 * 
	 * @param categoryId
	 * @param name
	 * @param l
	 */
	public void searchSpAppListOnStoreByName(final String categoryId,
			final String name, final AppStoreListener l) {
		new Thread(new Runnable() {

			public void run() {
				SearchAppListRq req = new SearchAppListRq();
				req.setPageInfo(new PageInfo(1, 100));
				if (TextUtils.isEmpty(name) == false) {
					Vector<Condition> conditions = new Vector<Condition>();
					Condition c = new Condition();
					c.setType(Condition.TYPE_NAME);
					c.setValue(name);
					conditions.add(c);
					if (categoryId != null
							&& categoryId.equals(MySpServiceDB.CATEGORY_ALL_ID) == false) {
						c = new Condition();
						c.setType(Condition.TYPE_CATEGORY);
						c.setValue(categoryId);
						conditions.add(c);
					}
					req.setConditionList(conditions);
				}
				SpAppGateWay gateway = context.getNetworkManager()
						.getSpAppGateWay();
				try {
					final SearchAppListRs res = gateway.searchServiceList(req);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {
									Vector<SpService> r = res.getServiceList();
									l.onList(r == null ? new Vector<SpService>(
											0) : r);
								} else {
									if (res != null && l != null) {
										l.onError(res.getResult().getCode(),
												res.getResult().getMessage());
									}
								}
							}
						});
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
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Search SP applications which are popular
	 * 
	 * @param categoryId
	 * @param l
	 */
	public void searchSpAppListOnStoreByPopular(final String categoryId,
			final AppStoreListener l) {
		new Thread(new Runnable() {

			public void run() {
				SearchAppListRq req = new SearchAppListRq();
				req.setPageInfo(new PageInfo(1, 100));
				Vector<Condition> conditions = new Vector<Condition>();
				Condition c = new Condition();
				c.setType(Condition.TYPE_IS_POPULAR);
				c.setValue("Y");
				conditions.add(c);
				Condition c2 = new Condition();
				c2.setType(Condition.TYPE_CATEGORY);
				c2.setValue(categoryId);
				conditions.add(c2);
				req.setConditionList(conditions);
				SpAppGateWay gateway = context.getNetworkManager()
						.getSpAppGateWay();
				try {
					final SearchAppListRs res = gateway.searchServiceList(req);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK) {
									Vector<SpService> r = res.getServiceList();
									l.onList(r == null ? new Vector<SpService>(
											0) : r);
								} else {
									if (res != null && l != null) {
										l.onError(res.getResult().getCode(),
												res.getResult().getMessage());
									}
								}
							}
						});
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
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Search all SP applications.
	 * 
	 * @param l
	 */
	public void searchAllSpAppsOnStore(final AppStoreListener l) {
		getSpAppListOnStoreByCategory(MySpServiceDB.CATEGORY_ALL_ID, l);
	}

	/**
	 * @param serviceId
	 * @return
	 * @throws ResNotOKException
	 * @throws NoNetworkException
	 * @throws NoResponseException
	 */
	public SpService getSpAppInfo(final String serviceId)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		ViewAppInfoRq req = new ViewAppInfoRq(serviceId);
		// DeviceInfo deviceInfo = new DeviceInfo(context);
		// String msisdn = deviceInfo.getMDN();
		// req.setIccid("89860110611108096225");// iccId get from
		// SharedPreference
		// req.setMsisdn("13581517431");
		req.setServiceId(serviceId);
		SpAppGateWay gateway = context.getNetworkManager().getSpAppGateWay();
		final ViewAppInfoRs res = gateway.viewServiceInfo(req);
		Log.d(TAG, "ResultCode>>>>>>>>>>>>>>>>" + res.getResult().getCode());
		if (res != null && res.getResult().getCode() == Result.OK) {
			return res.getService();
		} else {
			return null;
		}
	}

	/**
	 * Get detail information about a SP application.
	 * 
	 * @param serviceId
	 * @param l
	 */
	public void getServiceDetail(final String serviceId,
			final AppStoreListener l) {
		new Thread(new Runnable() {

			public void run() {
				try {
					ViewAppInfoRq req = new ViewAppInfoRq(serviceId);
					// DeviceInfo deviceInfo = new DeviceInfo(context);
					// String msisdn = deviceInfo.getMDN();
					// req.setIccid("89860110611108096225");// iccId get from
					// SharedPreference
					// req.setMsisdn("13581517431");
					req.setServiceId(serviceId);
					SpAppGateWay gateway = context.getNetworkManager()
							.getSpAppGateWay();
					final ViewAppInfoRs res = gateway.viewServiceInfo(req);
					// final SpService service = getSpAppInfo(serviceId);
					if (l != null) {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (res != null
										&& res.getResult().getCode() == Result.OK
										&& res.getService() != null) {
									l.onDetail(res.getService());
								} else {
									if (res != null && l != null) {
										l.onError(res.getResult().getCode(),
												res.getResult().getMessage());
									}
								}
							}
						});
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
				} catch (final ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onError(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Return currently registered SP applications on line including default
	 * applications.
	 * 
	 * @return if no SP applications, return empty vector
	 * @throws ResNotOKException
	 */
	public Vector<SpService> getMySpAppListOnStore() throws NoNetworkException,
			NoResponseException, ResNotOKException {
		SpAppGateWay spGw = context.getNetworkManager().getSpAppGateWay();
		GetMyAppListRs res;
		res = spGw.getMyServiceList();
		if (res != null && res.getResult().getCode() == Result.OK) {
			return res.getServiceList();
		}
		return null;
	}

	/**
	 * Return the SP applications having the AID list.
	 * 
	 * @param aidList
	 * @return
	 * @throws ResNotOKException
	 */
	public Vector<SpService> getGuiSpAppListOnStore(Vector<String> aidList)
			throws NoNetworkException, NoResponseException, ResNotOKException {
		Vector<SpService> list = new Vector<SpService>();
		if (aidList.size() > 0) {
			SpAppGateWay spGw = context.getNetworkManager().getSpAppGateWay();
			GetGuiAppListRq req = new GetGuiAppListRq(aidList);
			GetGuiAppListRs res;
			res = spGw.getGuiServiceList(req);
			if (res != null && res.getAppList() != null)
				return res.getAppList();
		}
		return list;
	}

	/**
	 * Return true if the package installed on device.
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isInstalledOnDevice(String packageName) {
		if (TextUtils.isEmpty(packageName))
			return false;
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * @return
	 */
	public MySpServiceDB getDatabase() {
		return database;
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}

	/**
	 * Checks if there is update for the service, which must have service ID and
	 * application version.
	 * 
	 * @param service
	 * @param l
	 */
	public void checkUpdateService(final SpService service,
			final ServiceUpdateListener l) {
		new Thread(new Runnable() {

			public void run() {
				CheckUpdateAppRq req = new CheckUpdateAppRq(
						service.getServiceId(), service.getAppVersion());
				try {
					CheckUpdateAppRs res = context.getNetworkManager()
							.getSpAppGateWay().checkUpdateApp(req);
					if (res != null && res.getResult().getCode() == Result.OK
							&& res.getUpdateYn().equalsIgnoreCase("Y")) {
						final boolean isOptional = res.getUpdateMandatoryYn()
								.equalsIgnoreCase("N");
						final String downUrl = res.getAppDownloadUrl();
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (l != null)
									l.onUpdate(isOptional, downUrl);
							}
						});
					} else {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								if (l != null)
									l.onNoUpdate();
							}
						});
					}
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null)
								l.onNoNetwork();
						}
					});
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null)
								l.onNoResponse();
						}
					});
				} catch (ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.onNoUpdate();
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * remove Auth Failed service
	 * */
	public void removeAuthFailedService(final Vector<String> serviceIdList,
			final RemoveAuthFailedServiceListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				RemoveAuthFailedRq req = new RemoveAuthFailedRq();
				req.setServiceIdList(serviceIdList);
				req.setWoAccountId(context.getSettingManager().getString(
						WalletManager.WO_ACCOUNT_ID, ""));
				try {
					RemoveAuthFailedRs res = context.getNetworkManager()
							.getSpAppGateWay().removeAuthFailedService(req);
					if (res.getResult().getCode() == Result.OK) {
						l.onSuccess();
					}
				} catch (NoNetworkException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null)
								l.onNoNetwork();
						}
					});
					return;
				} catch (NoResponseException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null)
								l.onNoResponse();
						}
					});
					return;
				} catch (final ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (l != null)
								l.onFailed(e.getCode(), e.getMessage());
						}
					});
					return;
				}
			}
		}).start();
	}

	private String transmitResult = null;
	private final Object lock = new Object();

	private void setTransmitResult(String result) {
		transmitResult = result;
	}

	private void notifyMyThread() {
		synchronized (lock) {
			lock.notify();
		}
	}

	public String transmit(final String str) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SpAppGateWay walletGateWay = context.getNetworkManager()
						.getSpAppGateWay();
				try {
					String res = walletGateWay.transmit(str);
					setTransmitResult(res);
					notifyMyThread();
				} catch (NoNetworkException e) {
					e.printStackTrace();
					setTransmitResult(NONETWORKEXCEPTION);
					notifyMyThread();
				} catch (NoResponseException e) {
					e.printStackTrace();
					setTransmitResult(NORESPONSEEXCEPTION);
					notifyMyThread();
				}
			}
		}).start();
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return transmitResult;
	}

	public void recordCustomerSpOrder(final String customerId,
			final String serviceId, final String orderId,
			final String writeResult, final RecordCustomerSpOrderListener l) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				RecordSpOrderRq rq = new RecordSpOrderRq();
				rq.setCustomerId(customerId);
				rq.setOrderId(orderId);
				rq.setServiceId(serviceId);
				rq.setWriteCardResult(writeResult);
				try {
					RecordSpOrderRs res = context.getNetworkManager()
							.getSpAppGateWay().recordCustomerSpOrder(rq);
					if (res.getResult().getCode() == Result.OK) {
						l.recordSuccess();
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
				} catch (ResNotOKException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.recordFailed();
						}
					});
					return;
				}
			}
		}).start();
	}
}

package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.db.MySpServiceDB;
import cn.unicompay.wallet.client.framework.model.SpService;
import cn.unicompay.wallet.client.framework.util.Util;

import com.google.gson.Gson;
import com.skcc.wallet.core.se.SException;
import com.skcc.wallet.core.se.SExceptionInfo;
import com.skcc.wallet.core.se.instance.CRSApplication;

public class CRSManager {

	private final static String TAG = "CRSManager";
	/** The CRS application instance reference **/
	private CRSApplication crsApp = null;
	/** Context **/
	private WApplication context;
	private CRSApplication.Application[] applications;
	private final static String CRSAPPLICATION = "crs_application";

	private final static class AppInfo {

		CRSApplication.Application[] list;
	}

	/**
	 * Constructor
	 * 
	 * @param crsApp
	 * @param context
	 */
	public CRSManager(CRSApplication crsApp, WApplication context) {
		this.crsApp = crsApp;
		this.context = context;
	}

	/**
	 * Returns the all applications registered in CRS application.
	 * 
	 * @return
	 * @see CRSApplication#getAllApplication()
	 */
	CRSApplication.Application[] getApps(boolean useCache) {
		// try {
		// return crsApp.getAllApplication();
		// } catch (SException e) {
		// e.printStackTrace();
		// return null;
		// }
		Log.d(TAG, "getApps>>useCache=" + useCache);
		try {
			if (applications == null) {
				String _app = context.getSettingManager().getString(
						CRSAPPLICATION, "");
				if (!TextUtils.isEmpty(_app) && useCache) {
					Gson gson = new Gson();
					AppInfo info = gson.fromJson(_app, AppInfo.class);
					applications = info.list;
				}
			}
			if (useCache && applications != null) {
				Log.d(TAG, "read from caChe>>>>>");
				return applications;
			}
			applications = crsApp.getAllApplication();
			Log.d(TAG, "read from Crs>>>>>");
			if (applications != null) {
				Gson gson = new Gson();
				AppInfo info = new AppInfo();
				info.list = applications;
				context.getSettingManager().putString(CRSAPPLICATION,
						gson.toJson(info));
			}
			return applications;
		} catch (SException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the application selected by aid.
	 * 
	 * @return
	 * @see CRSApplication#getApplication()
	 */
	CRSApplication.Application getApp(String aid) throws SException {
		return crsApp.getApplication(aid);
	}

	/**
	 * Returns the all applications' AIDs registered in CRS application.
	 * 
	 * @return
	 * @throws SException
	 * @see #getApps()
	 */
	Vector<String> getAllAid() {
		Vector<String> list = new Vector<String>();
		CRSApplication.Application[] apps = getApps(false);
		if (apps != null) {
			for (CRSApplication.Application app : apps) {
				list.add(app.aid);
			}
		}
		return list;
	}

	/**
	 * Turns contact-less interface on/off.
	 * 
	 * @param crsCB
	 * @param isTurnOn
	 *            if true, turns on interface. <br>
	 *            if false, turns off interface.
	 */
	public void setContactlessIF(final CRSResultCallback crsCB,
			final boolean isTurnOn) {
		// setContactlessIF
		new Thread(new Runnable() {

			public void run() {
				try {
					// if(
					// context.getSEManager().getPinManager().hasContactlessPin()
					// &&
					// context.getSEManager().getPinManager().getPINSession().hasValidSession()==false)
					// {
					// Util.makeLooperThread(new Runnable(){
					// public void run(){
					// if(crsCB!=null) crsCB.onNeedPIN();
					// }
					// });
					// return;
					// }
					SettingManager setting = context.getSettingManager();
					if (isTurnOn) {
						crsApp.turnOnContactlessIF();
						setting.putBoolean(CONTACTLESS_STATUS_KEY, true);
					} else {
						crsApp.turnOffContactlessIF();
						setting.putBoolean(CONTACTLESS_STATUS_KEY, false);
					}
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Checks if contact-less interface on or off
	 * 
	 * @return true - if contact-less is on
	 */
	public boolean getContactlessIF() {
		return context.getSettingManager().getBoolean(CONTACTLESS_STATUS_KEY,
				false);
	}

	/**
	 * Keyword for contactless setting
	 */
	final static String CONTACTLESS_STATUS_KEY = "CONTACTLESS_STATUS_KEY";

	/**
	 * Activates the application.
	 * 
	 * @param crsCB
	 * @param app
	 */
	public void activate(final CRSResultCallback crsCB, final SpService app) {
		// activate
		new Thread(new Runnable() {

			public void run() {
				try {
					Log.d(TAG, "activate>>>>>" + app.getServiceName());
					if (TextUtils.isEmpty(app.getAppletAid()))
						throw new SException();
					if (TextUtils.isEmpty(app.getServiceId())) {
						SpService app2 = context.getSpServiceManager()
								.getDatabase()
								.getSpAppByApplet(app.getAppletAid());
						if (app2 != null)
							app.setServiceId(app2.getServiceId());
					}
					// if(
					// context.getSEManager().getPinManager().hasActivatePin()
					// &&
					// context.getSEManager().getPinManager().getPINSession().hasValidSession()==false)
					// {
					// Util.makeLooperThread(new Runnable(){
					// public void run(){
					// if(crsCB!=null) crsCB.onNeedPIN();
					// }
					// });
					// return;
					// }
					crsApp.activateApp(app.getAppletAid());
					MySpServiceDB database = context.getSpServiceManager()
							.getDatabase();
					// app.setAppletState(SpService.INSTALLED_STATUS);
					app.setServiceSubscriptionState(SpService.INSTALLED_STATUS);
					database.updateAppletState(app);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * De-activates the application.
	 * 
	 * @param crsCB
	 * @param app
	 *            .
	 */
	public void deActivate(final CRSResultCallback crsCB, final SpService app) {
		// deActivate
		new Thread(new Runnable() {

			public void run() {
				try {
					Log.d(TAG, "deActivate serviceI>>>>" + app.getServiceId());
					if (TextUtils.isEmpty(app.getAppletAid()))
						throw new SException();
					if (TextUtils.isEmpty(app.getServiceId())) {
						SpService app2 = context.getSpServiceManager()
								.getDatabase()
								.getSpAppByApplet(app.getAppletAid());
						if (app2 != null)
							app.setServiceId(app2.getServiceId());
					}
					// if(
					// context.getSEManager().getPinManager().hasActivatePin()
					// &&
					// context.getSEManager().getPinManager().getPINSession().hasValidSession()==false)
					// {
					// Util.makeLooperThread(new Runnable(){
					// public void run(){
					// if(crsCB!=null) crsCB.onNeedPIN();
					// }
					// });
					// return;
					// }
					crsApp.deactivateApp(app.getAppletAid());
					MySpServiceDB database = context.getSpServiceManager()
							.getDatabase();
					// app.setAppletState(SpService.LOCKED_STATUS);
					database.updateAppletState(app);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * <pre>
	 * Set the application  to main card.
	 * </pre>
	 * 
	 * @param crsCB
	 * @param app
	 */
	public void setMainApp(final CRSResultCallback crsCB, final SpService app) {
		// setMainApp
		new Thread(new Runnable() {

			public void run() {
				try {
					if (TextUtils.isEmpty(app.getAppletAid()))
						throw new SException();
					if (TextUtils.isEmpty(app.getServiceId())) {
						SpService app2 = context.getSpServiceManager()
								.getDatabase()
								.getSpAppByApplet(app.getAppletAid());
						if (app2 != null)
							app.setServiceId(app2.getServiceId());
					}
					// update for China Unicom Payment
					// crsApp.setMainApp(app.getAppletAid());
					crsApp.activateApp(app.getAppletAid());
					SettingManager setting = context.getSettingManager();
					setting.putString(SPServiceManager.MAIN_APP_KEY,
							app.getServiceId());
					app.setPriority(SpService.MAIN_CARD_PRIORITY);
					context.getSpServiceManager().getDatabase()
							.updateSpPriority(app);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Set the application to main card during specific time like card session.
	 * 
	 * @param crsCB
	 * @param app
	 */
	public void setOneTimePayment(final CRSResultCallback crsCB,
			final SpService app) {
		// setOneTimePayment
		new Thread(new Runnable() {

			public void run() {
				try {
					if (TextUtils.isEmpty(app.getAppletAid()))
						throw new SException();
					if (TextUtils.isEmpty(app.getServiceId())) {
						SpService app2 = context.getSpServiceManager()
								.getDatabase()
								.getSpAppByApplet(app.getAppletAid());
						if (app2 != null)
							app.setServiceId(app2.getServiceId());
					}
					// if(
					// context.getSEManager().getPinManager().hasSinglePayPin()
					// &&
					// context.getSEManager().getPinManager().getPINSession().hasValidSession()==false)
					// {
					// Util.makeLooperThread(new Runnable(){
					// public void run(){
					// if(crsCB!=null) crsCB.onNeedPIN();
					// }
					// });
					// return;
					// }
					// update for China Unicom Payment
					// crsApp.setOneTimeMainApp(app.getAppletAid());
					crsApp.activateApp(app.getAppletAid());
					SettingManager setting = context.getSettingManager();
					setting.putString(SPServiceManager.VOLATILE_APP_KEY,
							app.getServiceId());
					setting.putInt(SPServiceManager.VOLATILE_APP_PRIORITY,
							app.getPriority());
					app.setPriority(SpService.VOLATILE_PRIORITY);
					context.getSpServiceManager().getDatabase()
							.updateSpPriority(app);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Reset the application to the previous card.
	 * 
	 * @param crsCB
	 * @param app
	 */
	public void resetOneTimePayment(final CRSResultCallback crsCB,
			final SpService app) {
		// resetOneTimePayment
		new Thread(new Runnable() {

			public void run() {
				try {
					if (TextUtils.isEmpty(app.getAppletAid()))
						throw new SException();
					if (TextUtils.isEmpty(app.getServiceId())) {
						SpService app2 = context.getSpServiceManager()
								.getDatabase()
								.getSpAppByApplet(app.getAppletAid());
						if (app2 != null)
							app.setServiceId(app2.getServiceId());
					}
					// if(
					// context.getSEManager().getPinManager().hasSinglePayPin()
					// &&
					// context.getSEManager().getPinManager().getPINSession().hasValidSession()==false)
					// {
					// Util.makeLooperThread(new Runnable(){
					// public void run(){
					// if(crsCB!=null) crsCB.onNeedPIN();
					// }
					// });
					// return;
					// }
					// update for China Unicom Payment
					// crsApp.resetOneTimeMainApp(app.getAppletAid());
					// SettingManager setting = context.getSettingManager();
					// int priority =
					// setting.getInt(SPServiceManager.VOLATILE_APP_PRIORITY,
					// SpService.NORMAL_PRIORITY);
					// setting.remove(SPServiceManager.VOLATILE_APP_KEY);
					// setting.remove(SPServiceManager.VOLATILE_APP_PRIORITY);
					// app.setPriority((short) priority);
					// context.getSpServiceManager().getDatabase().updateSpPriority(app);
					SettingManager setting = context.getSettingManager();
					SpService spService = context.getSpServiceManager()
							.getMainApp();
					if (spService != null) {
						crsApp.activateApp(spService.getAppletAid());
					} else {
						crsApp.deactivateApp(app.getAppletAid());
					}
					int priority = setting.getInt(
							SPServiceManager.VOLATILE_APP_PRIORITY,
							SpService.NORMAL_PRIORITY);
					setting.remove(SPServiceManager.VOLATILE_APP_KEY);
					setting.remove(SPServiceManager.VOLATILE_APP_PRIORITY);
					app.setPriority((short) priority);
					context.getSpServiceManager().getDatabase()
							.updateSpPriority(app);
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onSuccess();
						}
					});
				} catch (SException e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							if (crsCB != null)
								crsCB.onFail("");// put info object.
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Returns the application selected by aid.
	 * 
	 * @return
	 * @see CRSApplication#geApplication()
	 */
	public void existInCRS(final getApplicationListener l, final String aid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				CRSApplication.Application app = null;
				try {
					app = getApp(aid);
					if (app != null) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.onExist();
							}
						});
					} else {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.notExist();
							}
						});
					}
				} catch (SException e) {
					e.printStackTrace();
					if (e.getInformation().errCode == SExceptionInfo.REFERECED_DATA_NOT_FOUND) {
						Util.makeLooperThread(new Runnable() {

							@Override
							public void run() {
								l.notExist();
							}
						});
					} else {
						Util.makeLooperThread(new Runnable() {

							public void run() {
								l.noSE();
							}
						});
					}
					Log.d("CRSManager",
							"existInCRS>>>getMessage>>>" + e.getMessage());
					return;
				} catch (Exception e) {
					e.printStackTrace();
					Util.makeLooperThread(new Runnable() {

						public void run() {
							l.noSE();
						}
					});
				}
			}
		}).start();
	}

	public void getApplicationByAid(final getApplicationListener l, String aid) {
		CRSApplication.Application app = null;
		try {
			app = getApp(aid);
			if (app != null) {

				l.onExist();
			} else {

				l.notExist();
			}

		} catch (SException e) {
			e.printStackTrace();
			if (e.getInformation().errCode == SExceptionInfo.REFERECED_DATA_NOT_FOUND) {

				l.notExist();
			} else {
				l.noSE();
			}
			Log.d("CRSManager", "existInCRS>>>getMessage>>>" + e.getMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			l.noSE();
		}
	}
}

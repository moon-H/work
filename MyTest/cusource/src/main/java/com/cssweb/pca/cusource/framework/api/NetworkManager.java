package cn.unicompay.wallet.client.framework.api;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.api.http.EventGateWay;
import cn.unicompay.wallet.client.framework.api.http.InBoxGateWay;
import cn.unicompay.wallet.client.framework.api.http.PushGateWay;
import cn.unicompay.wallet.client.framework.api.http.SpAppGateWay;
import cn.unicompay.wallet.client.framework.api.http.WalletGateWay;
import cn.unicompay.wallet.client.framework.api.http.model.ChangeLanguageRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetSpListRs;
import cn.unicompay.wallet.client.framework.api.http.model.ViewCategoryRs;
import cn.unicompay.wallet.client.framework.model.Category;
import cn.unicompay.wallet.client.framework.model.SP;

import com.skcc.wallet.core.http.NetworkSession;
import com.skcc.wallet.core.http.ServerInfo;
import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;

public class NetworkManager implements NetworkSession {
	private static final String TAG = "NetworkManager";
	/** The application context **/
	private WApplication context;

	/** The server information **/
	protected ServerInfo serverInfo = null;
	/** The current cookie value **/
	private String cookie;
	/** The last session time **/
	private long lastSessionTime;

	public static final String WALLET_PACKAGE_NAME = "walletPkgName";

	/** The constant for no network connection **/
	public static final short NO_CONNECTION_AVAILABLE = 0x00;
	/** The constant for wifi connection **/
	public static final short WIFI_AVAILABLE = 0x01;
	/** The constant for mobile connection **/
	public static final short MOBILE_3G_AVAILABLE = 0x10;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param serverInfo
	 */
	public NetworkManager(WApplication context, ServerInfo serverInfo) {
		this.context = context;
		this.serverInfo = serverInfo;
		isOnLine = (checkAvailableNetworkStatus() != NO_CONNECTION_AVAILABLE);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param serverName
	 * @param serverUrl
	 */
	public NetworkManager(WApplication context, String serverName,
			String serverUrl) {
		this(context, new ServerInfo(serverName, serverUrl));
	}

	/**
	 * @return
	 */
	public long getLastSessionTime() {
		return lastSessionTime;
	}

	/**
	 * @param time
	 */
	public void setLastSessionTime(long time) {
		lastSessionTime = time;
	}

	/**
	 * <pre>
	 * Returns the current cookie.
	 * </pre>
	 * 
	 * @return
	 * @see SettingManager
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * <pre>
	 * Save the current cookie.
	 * </pre>
	 * 
	 * @param cookie
	 * @see SettingManager
	 */
	public void setCookie(String cookie) {
		if (TextUtils.isEmpty(cookie))
			return;
		this.cookie = cookie;
	}

	/**
	 * Returns the current available network status
	 * 
	 * @return
	 * @see #NO_CONNECTION_AVAILABLE
	 * @see #WIFI_AVAILABLE
	 * @see #MOBILE_3G_AVAILABLE
	 */
	public short checkAvailableNetworkStatus() {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		short result = NO_CONNECTION_AVAILABLE;
		if (wifi.isAvailable()) {
			result |= WIFI_AVAILABLE;
		}
		if (mobile.isAvailable()) {
			result |= MOBILE_3G_AVAILABLE;
		}

		Log.d(TAG, "network status: " + result);

		return result;
	}

	/**
	 * Return if the network is available.
	 * 
	 * @return
	 */
	public boolean isNetworkAvailable() {
		return isOnLine;
	}

	/**
	 * Start monitoring for changing network status.
	 */
	public void startNetworkMonitor() {
		context.registerReceiver(monitorNetwork, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		context.registerReceiver(localeReceiver, new IntentFilter(
				Intent.ACTION_LOCALE_CHANGED));
		context.registerReceiver(screenMonitor, new IntentFilter(
				Intent.ACTION_SCREEN_ON));
	}

	/**
	 * Stop monitoring for change network status.
	 */
	public void stopNetworkMonitor() {
		context.unregisterReceiver(monitorNetwork);
		context.unregisterReceiver(localeReceiver);
		context.unregisterReceiver(screenMonitor);
	}

	private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {
			/**
			 * String walletPkgName =
			 * context.getSettingManager().getString(WALLET_PACKAGE_NAME, "");
			 * if(TextUtils.isEmpty(walletPkgName)) return;
			 * 
			 * ActivityManager am = (ActivityManager)
			 * context.getSystemService(Context.ACTIVITY_SERVICE); // get the
			 * info from the currently running task // List <
			 * ActivityManager.RunningTaskInfo > taskInfo =
			 * am.getRunningTasks(1); Log.d("current task :",
			 * "CURRENT Activity ::" +
			 * taskInfo.get(0).topActivity.getClass().getSimpleName());
			 * ComponentName componentInfo = taskInfo.get(0).topActivity;
			 * if(componentInfo.getPackageName().equals(walletPkgName)==false)
			 * return;
			 */

			new Thread(new Runnable() {
				public void run() {
					WalletGateWay gateway = context.getNetworkManager()
							.getWalletGateWay();
					Log.d(TAG, "RequestToChangeLaunguage="
							+ Locale.getDefault().getLanguage());
					ChangeLanguageRq req = new ChangeLanguageRq(Locale
							.getDefault().getLanguage());
					try {
						gateway.changeLanguage(req);
					} catch (NoNetworkException e) {
						e.printStackTrace();
					} catch (NoResponseException e) {
						e.printStackTrace();
					} catch (ResNotOKException e) {
						e.printStackTrace();
					}

					SpAppGateWay spGw = context.getNetworkManager()
							.getSpAppGateWay();
					ViewCategoryRs categories;
					try {
						categories = spGw.viewCategory();
						if (categories != null) {
							Vector<Category> listOnLine = categories
									.getCategoryList();
							context.getSpServiceManager().setSPCategory(
									listOnLine);
						}

						// Get SP
						GetSpListRs spList = spGw.getSpList();
						if (spList != null) {
							Vector<SP> listOnLine = spList.getSpList();
							context.getSpServiceManager().setSPList(listOnLine);
						}
					} catch (NoNetworkException e1) {
						e1.printStackTrace();
					} catch (NoResponseException e1) {
						e1.printStackTrace();
					} catch (ResNotOKException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	};

	private BroadcastReceiver monitorNetwork = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			NetworkInfo info = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			isOnLine = (info != null && info.isConnected()) ? true : false;
		}
	};

	private BroadcastReceiver screenMonitor = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {

			Log.d(TAG, "screenMonitor onReceived wake lock ::");

			// if(context.getSEManager().getPinManager().hasWakeLockPin()==false)
			// return;

			String walletPkgName = context.getSettingManager().getString(
					WALLET_PACKAGE_NAME, "");
			if (TextUtils.isEmpty(walletPkgName))
				return;

			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			// get the info from the currently running task
			List<ActivityManager.RunningTaskInfo> taskInfo = am
					.getRunningTasks(1);
			Log.d(TAG, "screenMonitor current activity ::"
					+ taskInfo.get(0).topActivity.getClass().getSimpleName());
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(walletPkgName) == false)
				return;

			context.moveToPinScreen();
		}
	};

	/** The network online status **/
	private boolean isOnLine = false;

	/**
	 * @return Server Info.
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * This will simply call {@link WalletManager#getMobileLogin()}.
	 * 
	 * @return
	 * @see WalletManager#getMobileLogin()
	 */
	public boolean mobileAuthentication() {
		// 업무 협의 후 수정
		return context.getWalletManager().moblieAuth();
	}

	/**
	 * Return wallet gateway object
	 * 
	 * @return
	 */
	public WalletGateWay getWalletGateWay() {
		return new WalletGateWay(this);
	}

	/**
	 * @return
	 */
	public SpAppGateWay getSpAppGateWay() {
		return new SpAppGateWay(this);
	}

	/**
	 * @return
	 */
	public PushGateWay getPushGateWay() {
		return new PushGateWay(this);
	}

	/**
	 * @return
	 */
	public InBoxGateWay getInBoxGateWay() {
		return new InBoxGateWay(this);
	}

	public EventGateWay getEventGateWay() {
		return new EventGateWay(this);
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}

	@Override
	public String getServerUrl() {
		// return "https://116.90.86.163:8543/unicomwallet/ci";
		return WApplication.DEFAULT_SERVER_URL;
	}

	@Override
	public String getToken() {
		return null;
	}

	@Override
	public boolean mobileLogin() {
		return true;
	}

	@Override
	public boolean mobileToken() {
		return false;
	}

	private int keyStoreRes;

	public int getKeyStoreRes() {
		return keyStoreRes;
	}

	public void setKeyStoreRes(int keyStoreRes) {
		this.keyStoreRes = keyStoreRes;
	}

	@Override
	public KeyStore provideKS() {
		if (keyStoreRes <= 0)
			return null;

		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources()
					.openRawResource(keyStoreRes);
			try {
				trusted.load(in, "mysecret".toCharArray());
			} finally {
				in.close();
			}
			return trusted;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void setToken(String arg0) {

	}

}

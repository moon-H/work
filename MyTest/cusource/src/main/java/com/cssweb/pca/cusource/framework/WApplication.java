package cn.unicompay.wallet.client.framework;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import cn.unicompay.wallet.client.framework.api.EventManager;
import cn.unicompay.wallet.client.framework.api.InBoxManager;
import cn.unicompay.wallet.client.framework.api.NetworkManager;
import cn.unicompay.wallet.client.framework.api.SEManager;
import cn.unicompay.wallet.client.framework.api.SPServiceManager;
import cn.unicompay.wallet.client.framework.api.SettingManager;
import cn.unicompay.wallet.client.framework.api.WalletManager;

import com.skcc.wallet.core.http.ServerInfo;

public class WApplication extends Application {

	// ### for DEV server
	/**
	 * 生产环境
	 * */
	// public static final String DEFAULT_SERVER_ADDRESS
	// ="wallet.unicompayment.com";// 生产;Production
	// public static final String DEFAULT_PORT = "8543";// 生产;Production
	// /** The server IP for CorPay server **/
	// public static final String DEFAULT_SERVER_IP = "https://" +
	// DEFAULT_SERVER_ADDRESS;
	/**
	 * 开发环境
	 * */

	// public static final String DEFAULT_SERVER_ADDRESS = "116.90.86.163";//
	// 测试;Development

	/**
	 * 集成环境
	 * */
	public static final String DEFAULT_SERVER_ADDRESS = "123.125.97.93";//
	// Integration
	public static final String DEFAULT_PORT = "8180";// 集成1
	/** The server IP for CorPay server **/
	public static final String DEFAULT_SERVER_IP = "http://"
			+ DEFAULT_SERVER_ADDRESS;

	// ------------------------------------------------------------------
	// public static final String DEFAULT_PORT = "8380";// 集成2
	public static final String TEST_MDN = "01057190946"; // - only for test
	/** The wallet application ID **/
	public static final String WALLET_APP_ID = "000001";
	/** The server name **/
	public static final String DEFAULT_SERVER_NAME = "corpay server";
	/** The URL for CorPay server **/
	public static final String DEFAULT_SERVER_URL = DEFAULT_SERVER_IP + ":"
			+ DEFAULT_PORT + "/unicomwallet/ci";
	/** The singleton object of WalletManager **/
	private static WalletManager wm = null;
	/** The singleton object of SEManager **/
	private static SEManager sem = null;
	/** The singleton object of NetworkManager **/
	private static NetworkManager network = null;
	/** The singleton object of SettingManager **/
	private static SettingManager setting = null;
	/** The singleton object of InBoxManager **/
	private static InBoxManager inbox = null;
	/** The singleton object of SPAppManager **/
	private static SPServiceManager spApp = null;
	/** The singleton object of PushNoticeManager **/
	// phase1 no use push
	// private static PushNoticeManager pushMgr = null;
	/** The singleton object of PushNoticeManager **/
	private static EventManager eventMgr = null;
	// private static ImageLoader imageLoader;
	// check sp's openChannel call
	private byte aid[];
	private String woAccountId;
	private String HRV;
	private String name;// realName
	private String idType;
	private String idValue;
	private String phoneNo;
	/**
	 * Used by qu na
	 * */
	private String eticket_aid;
	private String eticket_serviceId;
	private String eticket_apdu;
	private String eticket_successMsg;
	private String eticket_failMsg;
	private String eticket_status;// 01:success,02:fail,03:Applet Not Exist
	private String eticket_orderId;
	/**
	 * define Use Pin Y/N
	 */
	private boolean IsUsePin = false;
	private static final String TAG = "WApplication";

	// 未经过正常登录流程进入主页时提示非法用户。
	private boolean isIllegalUser = true;

	/**
	 * 
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate>>>>>>>>>>>>>>>>>>>>>");
		sem = new SEManager(this);
		sem.connect();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		wm = null;
		network.stopNetworkMonitor();
		network = null;
		setting = null;
		inbox = null;
		spApp = null;
		// phase1 no use push
		// pushMgr = null;
		eventMgr = null;
	}

	/**
	 * Returns the singleton of WalletManager.
	 * 
	 * @return
	 */
	public WalletManager getWalletManager() {
		if (wm == null) {
			wm = new WalletManager(this, WALLET_APP_ID);
		}
		return wm;
	}

	/**
	 * Returns the singleton of SEManager.
	 * 
	 * @return
	 */
	public SEManager getSEManager() {
		// register PINVerificationObserver to PIN manager
		// if (sem == null) {
		// sem = new SEManager(this);
		// }
		return sem;
	}

	/**
	 * Returns the singleton of NetworkManager.
	 * 
	 * @return
	 */
	public NetworkManager getNetworkManager() {
		if (network == null) {
			network = new NetworkManager(this, new ServerInfo(
					DEFAULT_SERVER_NAME, DEFAULT_SERVER_URL));
			network.startNetworkMonitor();
		}
		return network;
	}

	/**
	 * Returns the singleton of SettingManager.
	 * 
	 * @return
	 */
	public SettingManager getSettingManager() {
		if (setting == null) {
			setting = new SettingManager(this);
		}
		return setting;
	}

	/**
	 * @return
	 */
	// phase1 no use push
	// public PushNoticeManager getPushNoticeManager() {
	// if (pushMgr == null) {
	// pushMgr = new PushNoticeManager(this);
	// }
	// return pushMgr;
	// }
	/**
	 * <pre>
	 * application change to PIN screen to verify PIN.
	 * PIN verification activity must have intent filter.
	 * <li> action : cn.unicompay.wallet.intent.action.PIN_VERIFICATION
	 * <li> category : cn.unicompay.wallet.intent.category.DEFAULT
	 * </pre>
	 */
	public void moveToPinScreen() {
		Log.d(TAG, "moveToPinScreen>> ");
		// if UsePin = false then return;
		if (!IsUsePin)
			return;
		Intent intent = new Intent(
				"cn.unicompay.wallet.intent.ACTION_PIN_VERIFICATION");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * Returns the singleton of InBoxManager.
	 * 
	 * @return
	 */
	public InBoxManager getInBoxManager() {
		if (inbox == null) {
			inbox = new InBoxManager(this);
		}
		return inbox;
	}

	/**
	 * @return
	 */
	public SPServiceManager getSpServiceManager() {
		if (spApp == null) {
			spApp = new SPServiceManager(this);
		}
		return spApp;
	}

	/**
	 * @return
	 */
	public EventManager getEventManager() {
		if (eventMgr == null) {
			eventMgr = new EventManager(this);
		}
		return eventMgr;
	}

	/**
	 * Returns the singleton of WalletManager.
	 * 
	 * @return
	 */

	public byte[] getAid() {
		return aid;
	}

	public String getEticket_aid() {
		return eticket_aid;
	}

	public void setEticket_aid(String eticket_aid) {
		this.eticket_aid = eticket_aid;
	}

	public String getEticket_serviceId() {
		return eticket_serviceId;
	}

	public void setEticket_serviceId(String eticket_serviceId) {
		this.eticket_serviceId = eticket_serviceId;
	}

	public String getEticket_apdu() {
		return eticket_apdu;
	}

	public void setEticket_apdu(String eticket_apdu) {
		this.eticket_apdu = eticket_apdu;
	}

	public String getEticket_successMsg() {
		return eticket_successMsg;
	}

	public void setEticket_successMsg(String eticket_successMsg) {
		this.eticket_successMsg = eticket_successMsg;
	}

	public String getEticket_failMsg() {
		return eticket_failMsg;
	}

	public void setEticket_failMsg(String eticket_failMsg) {
		this.eticket_failMsg = eticket_failMsg;
	}

	public String getEticket_status() {
		return eticket_status;
	}

	public void setEticket_status(String eticket_status) {
		this.eticket_status = eticket_status;
	}

	public String getEticket_orderId() {
		return eticket_orderId;
	}

	public void setEticket_orderId(String eticket_orderId) {
		this.eticket_orderId = eticket_orderId;
	}

	public void setAid(byte[] aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getWoAccountId() {
		return woAccountId;
	}

	public void setWoAccountId(String woAccountId) {
		this.woAccountId = woAccountId;
	}

	public String getHRV() {
		return HRV;
	}

	public void setHRV(String hRV) {
		HRV = hRV;
	}

	public boolean isIllegalUser() {
		return isIllegalUser;
	}

	public void setIllegalUser(boolean isIllegalUser) {
		this.isIllegalUser = isIllegalUser;
	}

	// public String getMobileId() {
	// return mobileId;
	// }
	//
	// public void setMobileId(String mobileId) {
	// this.mobileId = mobileId;
	// }

}

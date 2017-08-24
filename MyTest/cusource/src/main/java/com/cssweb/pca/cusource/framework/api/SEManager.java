package cn.unicompay.wallet.client.framework.api;

import java.io.IOException;
import java.util.Arrays;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.model.PINEvent;
import cn.unicompay.wallet.client.framework.util.Util;

import com.skcc.wallet.core.se.ISEMedia;
import com.skcc.wallet.core.se.SEConnectionListener;
import com.skcc.wallet.core.se.SException;
import com.skcc.wallet.core.se.instance.CRSApplication;
import com.skcc.wallet.core.se.instance.CardManager;
import com.skcc.wallet.core.se.instance.OthersZJ;
import com.skcc.wallet.core.se.instance.PBOC;
import com.skcc.wallet.core.se.instance.PINBlockedException;
import com.skcc.wallet.core.se.instance.Paypass;
import com.skcc.wallet.core.se.instance.TransitBJ;
import com.skcc.wallet.core.se.instance.Vsdc;
import com.skcc.wallet.core.se.instance.WalletApplet;
import com.skcc.wallet.core.se.uicc.UICCSimAlliance;

public class SEManager implements SEConnectionListener {

	/** The channel for SE operation **/
	private ISEMedia seMedia = null;
	/** The Card Manager instance reference **/
	private CardManager cardMgr = null;
	/** The CRS application instance reference **/
	private CRSApplication crsApp = null;
	/** The wallet application instance reference **/
	private WalletApplet walletApp = null;
	/** The listener for being ready for SE operation such as connection to SE **/
	/** The android application context **/
	private SEConnectionListener seReadyListener = null;
	private WApplication context;
	/** The wallet Id **/
	private String mobileId;
	/** channel **/
	private String channel;
	public static final int VISA = 0;
	public static final int MASTER = 1;
	public static final int PBOC_CREDIT = 2;
	public static final int PBOC_DEBIT = 3;
	public static final int PBOC_ECASH = 4;
	public static final int TRANSIT = 5;
	public static final int NON_PAYMENT_CARD = 6;
	public static final int OTHERS = 7;
	// public static final int TYPEA_CARD = 5;
	// public static final int TYPEB_CARD = 6;
	/** **/
	private boolean isRunning = false;

	/**
	 * <pre>
	 * Constructor.
	 * Creates SE instances.
	 * </pre>
	 * 
	 * @param c
	 */
	public SEManager(WApplication c) {
		// creates CardManager, CRSApplication, WalletApplet instance.
		this.context = c;
		this.seMedia = new UICCSimAlliance(c);
		this.cardMgr = new CardManager(seMedia);
		this.crsApp = new CRSApplication(seMedia);
		this.walletApp = new WalletApplet(seMedia);
		isRunning = true;
	}

	/**
	 * <pre>
	 * Connects to SE using SE Media.
	 * If a SEReadyListener is registered, SEReadyListener.onReady() is called in another thread.
	 * The registered SEReadyListenere will automatically be released,in other words, the reference will be destroyed.
	 * </pre>
	 * 
	 * @see {@link SEConnectionListener#onSEConnected()}
	 * @see ISEMedia#openConnection()
	 */
	public void connect() {
		isRunning = true;
		if (hasSEConnection())
			return;
		// connect to SE with ISEMedia
		// and call the registered SEReadyListener#onReady().
		// SEReadyListener#onReady() must be called on UI main thread.
		// and then release the SEReadyListener.
		Log.d("SEManager", "SeMedia>>>>>>>>>>" + seMedia);
		seMedia.setSEListener(this);
		try {
			boolean b = seMedia.openConnection();
			Log.d("SEManager", "SeMedia>>>>>>>>>>" + b);
		} catch (IOException e) {
			e.printStackTrace();
			onSEConnectionFail();
		}
	}

	public boolean isSEConnected() {
		return seMedia.isConnected();
	}

	public void closeSEChannel() {
		Log.d("SEManager", "CloseChannel>>>>>>>>>>>>>>>>" + channel);
		seMedia.closeChannel();
	}

	/**
	 * Disconnects the connection to SE.
	 */
	public void disconnect() {
		isRunning = false;
		if (seMedia.isConnected()) {
			try {
				seMedia.closeConnection();
				Log.d("SEManager", "disconnect>>>>>>>>>>>>>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// /**
	// * The singleton PINManger object.
	// */
	// private PINManager pinMgr = null;
	// /**
	// * <pre>
	// * Returns PIN manager.
	// * </pre>
	// * @return PIN manager instance if SE connected.<br>
	// * null if there is no SE connection.
	// * @see PINManager
	// */
	// public PINManager getPinManager(){
	// // if connection is not done, return null;
	// if(seMedia.isConnected()==false ) return null;
	// // check pinMgr is null. if it is null, create PINManager
	// if(pinMgr==null) {
	// pinMgr = new PINManager(walletApp,context);
	// }
	// return pinMgr;
	// }
	/**
	 * The singleton CRSManager object.
	 */
	private CRSManager crsMgr = null;

	/**
	 * Returns CRS Manager.
	 * 
	 * @return CRS manager instance if SE connected.<br>
	 *         null if there is no SE connection.
	 * @see CRSManager
	 */
	public CRSManager getCrsManager() {
		// if connection is not done, return null;
		// check crsMgr is null. if it is null, create CRSManager
		if (seMedia.isConnected() == false)
			return null;
		if (crsMgr == null) {
			crsMgr = new CRSManager(crsApp, context);
		}
		return crsMgr;
	}

	/**
	 * Returns the CPLC.
	 * 
	 * @return
	 */
	public String getCplc() {
		try {
			return cardMgr.getCPLC();
		} catch (SException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the mobile ID.
	 * 
	 * @return
	 */
	public String getMobileId() throws Exception {
		if (TextUtils.isEmpty(mobileId) == false)
			return mobileId;
		mobileId = walletApp.getWalletId();
		return mobileId;
	}

	void clearMobileId() {
		mobileId = null;
	}

	// TODO: �곸꽭 援ы쁽��寃�
	public String getWalletAppVersion() {
		return "will return WALLET_APP_VERSION";
	}

	/**
	 * <pre>
	 * Makes a cryptogram.
	 * </pre>
	 * 
	 * @param HRV
	 * @throws PINBlockedException
	 * @throws SException
	 */
	void makeCryptogram(String HRV) throws SException, PINBlockedException {
		walletApp.makeCryptogram(HRV);
	}

	/**
	 * Verifies mutual authentication.
	 * 
	 * @param HMAC
	 * @return true if authentication successes.
	 */
	boolean confirmCryptogram(String HMAC) {
		try {
			return walletApp.confirmCryptogram(HMAC);
		} catch (SException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the MAC on current session.
	 * 
	 * @return
	 */
	String getCMAC() {
		return walletApp.getCMac();
	}

	/**
	 * Returns the CRV on current session.
	 * 
	 * @return
	 */
	String getCRV() {
		return walletApp.getCrv();
	}

	/**
	 * Register the SE ready event listener.
	 * 
	 * @param l
	 * @see SEConnectionListener
	 */
	public void setSEConnectionListener(SEConnectionListener l) {
		this.seReadyListener = l;
	}

	/**
	 * <pre>
	 * Notify events to this manager.
	 * If the event is SCREEN_ON or SCREEN_OFF, it might trigger {@link WApplication#moveToPinScreen()}.
	 * The {@link PINEvent#EVT_CRS_OPERATION} comes from CRSManager.
	 * The {@link PINEvent#EVT_PIN_OPERTATION} and {@link PINEvent#EVT_PIN_VERIFICATION} comes from PIN manager.
	 * </pre>
	 * 
	 * @param evtsrc
	 * @param evtType
	 * @see PINEvent#EVT_CRS_OPERATION
	 * @see PINEvent#EVT_SCREEN_OFF
	 * @see PINEvent#EVT_SCREEN_ON
	 * @see PINEvent#EVT_PIN_OPERTATION
	 * @see PINEvent#EVT_PIN_VERIFICATION
	 * @see PINSession#logEvent(cn.unicompay.wallet.client.framework.model.PINEvent)
	 * @see PINVerificationObserver
	 * @see WApplication#moveToPinScreen()
	 */
	public void notifyEventForPIN(String evtsrc, short evtType) {
		// if session is not null, session logs the event.
		// if
	}

	/**
	 * Start PIN session.
	 */
	public void startPINSession() {
		// check if application use a pin session time,
		// and if session is null, start session.
	}

	// /**
	// * @return true if session is null or has valid session.
	// */
	// public boolean hasValidSession(){
	// return getPinManager().getPINSession().hasValidSession();
	// }
	@Override
	public void onSEConnected() {
		Log.d("SEManager", "onSEConnected>>>>>>>>>>>>>>>>>");
		// checks if SE really works.
		// String cplc = getCplc();
		// if(!TextUtils.isEmpty(cplc)) {
		new Thread(new Runnable() {

			public void run() {
				Util.makeLooperThread(new Runnable() {

					public void run() {
						if (seReadyListener != null) {
							seReadyListener.onSEConnected();
							seReadyListener = null;
						}
					}
				});
			}
		}).start();
		// }
	}

	@Override
	public void onSEConnectionFail() {
		Log.d("SEManager", "onSEConnectionFail>>>>>>>>>>>>>>>>>");
		if (seMedia != null) {
			disconnect();
		}
		new Thread(new Runnable() {

			public void run() {
				Util.makeLooperThread(new Runnable() {

					public void run() {
						if (seReadyListener != null) {
							seReadyListener.onSEConnectionFail();
							seReadyListener = null;
						}
					}
				});
			}
		}).start();
	}

	/**
	 * @return
	 */
	public boolean hasSEConnection() {
		return (seMedia != null && seMedia.isConnected());
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}

	@Override
	public void onSEDisconnected() {
		Log.d("SEManager", "onSEDisconnected>>>>>>>>>>>>>>>>>");
		if (isRunning) {
			connect();
		} else {
			seMedia.setSEListener(null);
			seMedia = null;
		}
	}

	/**
	 * @param cardType
	 * @param AID
	 * @return
	 * @throws RemoteException
	 */
	public String getCardNumber(int cardType, String AID) {
		switch (cardType) {
		case VISA:
			Log.d("SEmanager", "VISA>>>>>");
			Vsdc visa = new Vsdc(seMedia, AID);
			try {
				visa.getData();
				return visa.getCardNumber();
			} catch (Exception e) {
				e.printStackTrace();
			}
		case MASTER:
			Log.d("SEmanager", "MASTER>>>>>");
			Paypass master = new Paypass(seMedia, AID);
			try {
				master.getData();
				return master.getCardNumber();
			} catch (Exception e) {
				e.printStackTrace();
			}
		case PBOC_CREDIT:
		case PBOC_DEBIT:
		case PBOC_ECASH:
			Log.d("SEmanager", "PBOC_ECASH>>>>>");
			PBOC pboc = new PBOC(seMedia, AID);
			try {
				pboc.getData();
				return pboc.getCardNumber();
			} catch (Exception e) {
				e.printStackTrace();
			}
		case TRANSIT:
			Log.d("SEmanager", "TRANSIT>>>>>");
			TransitBJ transitBJ = new TransitBJ(seMedia, AID);
			try {
				return transitBJ.getCardNumber();
			} catch (SException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		case OTHERS:
			Log.d("SEmanager", "OTHERS>>>>>>");
			OthersZJ othersZJ = new OthersZJ(seMedia, AID);
			try {
				return othersZJ.getCardNumber();
			} catch (SException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
		default:
			return "";
		}
	}

	/**
	 * @param cardType
	 * @param AID
	 * @return
	 * @throws RemoteException
	 */
	public String getCardHolderName(int cardType, String AID) {
		switch (cardType) {
		case VISA:
			Vsdc visa = new Vsdc(seMedia, AID);
			visa.getData();
			return visa.getHolderName();
		case MASTER:
			Paypass master = new Paypass(seMedia, AID);
			master.getData();
			return master.getHolderName();
		case PBOC_CREDIT:
		case PBOC_DEBIT:
		case PBOC_ECASH:
			PBOC pboc = new PBOC(seMedia, AID);
			pboc.getData();
			return pboc.getHolderName();
		default:
			return "";
		}
	}

	/**
	 * @param cardType
	 * @param AID
	 * @return
	 * @throws RemoteException
	 */
	public String getExpirationDate(int cardType, String AID) {
		switch (cardType) {
		case VISA:
			Vsdc visa = new Vsdc(seMedia, AID);
			visa.getData();
			return visa.getExpirationDate();
		case MASTER:
			Paypass master = new Paypass(seMedia, AID);
			master.getData();
			return master.getExpirationDate();
		case PBOC_CREDIT:
		case PBOC_DEBIT:
		case PBOC_ECASH:
			PBOC pboc = new PBOC(seMedia, AID);
			pboc.getData();
			return pboc.getExpirationDate();
		default:
			return "";
		}
	}

	public String getBalance(int cardType, String AID) {
		switch (cardType) {
		case VISA:
		case MASTER:
			return "";
		case PBOC_CREDIT:
		case PBOC_DEBIT:
		case PBOC_ECASH:
			PBOC pboc = new PBOC(seMedia, AID);
			pboc.getData();
			try {
				return pboc.getBalance();
			} catch (SException e) {
				e.printStackTrace();
				return null;
			}
		case TRANSIT:
			TransitBJ transitBJ = new TransitBJ(seMedia, AID);
			try {
				return transitBJ.getBalance() + "";
			} catch (SException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			return "";
		}
	}

	//
	public byte[] openSEChannel(byte aid[]) {
		Log.d("SEManager", "openChannel>>>>>>>>>>>");
		byte response[] = (byte[]) null;
		// openSEChannel ���몄텧��SP 媛�留욌뒗吏�泥댄겕�쒕떎.
		// TODO: 以묎뎅�� �곸뼱 二쇱꽍�쇰줈 諛붽�寃�
		byte[] callerAid = context.getAid();
		if (!Arrays.equals(callerAid, aid)) {
			return response;
		}
		try {
			channel = null;
			response = seMedia.openChannel(aid);
		} catch (SException e) {
			e.printStackTrace();
		}
		channel = response.toString();
		return response;
	}

	public byte[] transceiveAPDU(byte cmd[]) {
		byte response[] = (byte[]) null;
		try {
			response = seMedia.exchangeAPDU(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 
	 * @param AID
	 * @return
	 * @throws RemoteException
	 */
	public String getCVN(String AID) {
		PBOC pboc = new PBOC(seMedia, AID);
		pboc.getData();
		try {
			return pboc.getCVN();
		} catch (SException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

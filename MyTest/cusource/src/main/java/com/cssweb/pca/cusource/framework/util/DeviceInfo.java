package cn.unicompay.wallet.client.framework.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import cn.unicompay.wallet.client.framework.WApplication;

public class DeviceInfo {
	/** android context **/
	private Context context;

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public DeviceInfo(Context c) {
		this.context = c;
	}

	/**
	 * Returns the device model name.
	 * 
	 * @return
	 */
	public String getDeviceModelName() {
		return android.os.Build.MODEL;
	}

	/**
	 * Returns the IMEI.
	 * 
	 * @return
	 */
	public String getIMEI() {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * Return the MDN.
	 * 
	 * @return
	 */
	public String getMDN() {
		String mdn = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		return TextUtils.isEmpty(mdn) ? WApplication.TEST_MDN : mdn; // TODO
																		// temporary
																		// code
	}

	/**
	 * Return the device ID.
	 * 
	 * @return
	 */
	public String getDeviceId() {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * Return the name of MNO.
	 * 
	 * @return
	 */
	public String getMNO() {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE))
				.getNetworkOperatorName();
	}

	/**
	 * Return the ICCID
	 * 
	 * @return
	 */
	public String getICCID() {
		return ((TelephonyManager) context.getSystemService("phone"))
				.getSimSerialNumber();
	}

	/**
	 * Return the imsi
	 * 
	 * @return
	 */
	public String getIMSI() {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}

}

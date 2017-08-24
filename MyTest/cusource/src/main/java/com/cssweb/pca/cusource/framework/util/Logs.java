package cn.unicompay.wallet.client.framework.util;


import android.util.Log;


public class Logs {
	public static void Log(boolean flag, String tag, String msg) {
		if (flag) {
			Log.d(tag, msg);
		}
	}
}

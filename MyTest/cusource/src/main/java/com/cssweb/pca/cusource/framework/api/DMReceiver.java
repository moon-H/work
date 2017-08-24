package cn.unicompay.wallet.client.framework.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.model.SpService;

/**
 * <pre>
 * Receiver for installing/updating/removing specific packages.
 * This receiver updates database, sends broadcasts, and send acknowledge back to server.
 * View needs registration receiver for broadcasting. The delivered intent has two extras.
 * <li> WalletManager.EXTRA_SP_APP_ID
 * <li> WalletManager.EXTRA_SP_OP_RESULT
 * </pre>
 * 
 * @author hj21
 * 
 */
public class DMReceiver extends BroadcastReceiver {
	private static final String TAG = "DMReceiver(framework)";

	@Override
	public void onReceive(Context context, Intent intent) {
		// update db, send broadcast to activities.

		if (Intent.ACTION_PACKAGE_INSTALL.equals(intent.getAction())
				|| Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())
				|| Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
			int uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
			Log.d(TAG, "onReceive>> UID :: " + uid);

			PackageManager packageManager = context.getPackageManager();

			String packageName = packageManager.getNameForUid(uid);
			Log.v(TAG, "onReceive>> app name (for uid) is : " + packageName);
			WApplication application = (WApplication) context
					.getApplicationContext();
			SpService app = application.getSpServiceManager().getDatabase()
					.getSpAppByPackage(packageName);
			if (app != null) {
				Log.d(TAG, "app is not null>>>>>>>>>>>>>>");
				sendBroadcastToActivities(context,
						WalletManager.SP_APP_INSTALLED_ACTION,
						app.getServiceId(), true);
			}

			// check wallet update
			String walletPkg = application.getSettingManager().getString(
					NetworkManager.WALLET_PACKAGE_NAME, "");
			if (!TextUtils.isEmpty(walletPkg) && walletPkg.equals(packageName)) {
				sendBroadcastToActivities(context,
						WalletManager.WALLET_UPDATE_ACTION, null, true);
			}

		}
	}

	/**
	 * Send broadcasts to activities
	 * 
	 * @param context
	 * @param action
	 * @param serviceId
	 */
	private void sendBroadcastToActivities(Context context, String action,
			String serviceId, boolean isSuccess) {
		Intent broadcast = new Intent(action);
		if (!TextUtils.isEmpty(serviceId))
			broadcast.putExtra(WalletManager.EXTRA_SP_APP_ID, serviceId);
		broadcast.putExtra(WalletManager.EXTRA_SP_OP_RESULT,
				isSuccess ? "success" : "fail");
		context.sendBroadcast(broadcast); // send broadcast to activities
		Log.v(TAG, "sendBroadcastToActivities>> service :: " + serviceId
				+ ", result OK::" + isSuccess);
	}

}

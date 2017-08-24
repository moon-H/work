package cn.unicompay.wallet.client.framework.dm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.api.WalletManager;

public class DMService extends Thread {
	private static final String TAG = "DMService";

	private final Activity activity;
	private final String downUrl;
	private final String serviceId;
	private final String action;

	/**
	 * @param c
	 * @param url
	 * @param serviceId
	 * @param action
	 */
	public DMService(Activity c, String url, String serviceId, String action) {
		this.activity = c;
		this.downUrl = url;
		this.serviceId = serviceId;
		this.action = action;
	}

	public void startDownload() {
		final long current = System.currentTimeMillis();
		final String filePath = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
				+ "/" + current + ".apk";
		File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String[] children = dir.list();
		if (children != null && children.length > 0) {
			for (String file : children) {
				if (file.endsWith(".apk")) {
					new File(dir, file).delete();
				}
			}
		}

		try {
			URL myFileUrl = new URL(downUrl);
			// --------------------------------------
			 HttpGet httpRequest = null;
			 httpRequest = new HttpGet(myFileUrl.toURI());
			
			 HttpClient httpclient = SSLSocketFactoryEx.getNewHttpClient();
			 HttpResponse response = (HttpResponse) httpclient
			 .execute(httpRequest);
			
			 HttpEntity entity = response.getEntity();
			 BufferedHttpEntity bufHttpEntity = new
			 BufferedHttpEntity(entity);
			 InputStream instream = bufHttpEntity.getContent();
			// ----------------------------------------------
			// -----------------------------
			// URLConnection conn = myFileUrl.openConnection();
			// conn.connect();
			// InputStream instream = conn.getInputStream();
			// -----------------------------
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			int count = 0;
			byte data[] = new byte[1024 * 1];

			while ((count = instream.read(data)) != -1) {
				Log.d(TAG, "下载count>>>>>>>>>" + count);
				fos.write(data, 0, count);
			}
			fos.flush();
			fos.close();
			instream.close();
			install(filePath);
		} catch (Exception e) {
			e.printStackTrace();

			sendBroadcastToActivities();
		}
	}

	/**
	 * Send broadcasts to activities
	 */
	private void sendBroadcastToActivities() {
		Intent broadcast = new Intent(action);
		if (!TextUtils.isEmpty(serviceId))
			broadcast.putExtra(WalletManager.EXTRA_SP_APP_ID, serviceId);
		broadcast.putExtra(WalletManager.EXTRA_SP_OP_RESULT, "fail");
		broadcast.putExtra(WalletManager.EXTRA_UPDATE_RESULT, "fail");
		activity.sendBroadcast(broadcast); // send broadcast to activities
		Log.v(TAG, "sendBroadcastToActivities>> service :: " + serviceId
				+ ", result fails::");
	}

	public void install(final String file) {
		File f = new File(file);
		Uri uri = Uri.fromFile(f);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		int requestCode = 0;
		if (action.equals(WalletManager.SP_APP_INSTALL_ACTION)) {
			requestCode = WalletManager.ACTION_INSTALL_REQUEST;
		} else if (action.equals(WalletManager.SP_APP_UPDATE_ACTION)) {
			requestCode = WalletManager.ACTION_UPDATE_REQEUST;
		} else if (action.equals(WalletManager.WALLET_UPDATE_ACTION)) {
			requestCode = WalletManager.ACTION_WALLET_UPDATE_REQUEST;
		}
		if (requestCode > 0)
			activity.startActivityForResult(intent, requestCode);
	}

	public void run() {
		startDownload();
	}

	/**
	 * @return
	 */
	public static boolean isWritableExternalDisk() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return true;
		return false;
	}

}

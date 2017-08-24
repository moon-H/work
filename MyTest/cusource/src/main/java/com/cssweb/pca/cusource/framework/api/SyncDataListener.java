package cn.unicompay.wallet.client.framework.api;

public interface SyncDataListener extends NetworkListener {
	public void onFinishedSync();

	public void onNoSE();

	public void onFailed(int errorCode, String errorMsg);
}

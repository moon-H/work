package cn.unicompay.wallet.client.framework.api;

public interface RemoveAuthFailedServiceListener extends NetworkListener {
	public void onSuccess();

	public void onFailed(int errorCode, String errorMsg);
}

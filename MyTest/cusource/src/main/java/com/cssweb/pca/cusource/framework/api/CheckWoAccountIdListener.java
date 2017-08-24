package cn.unicompay.wallet.client.framework.api;

public interface CheckWoAccountIdListener extends NetworkListener {

	public void onSuccess();

	public void onFail(int errorCode, String errorMsg);
}

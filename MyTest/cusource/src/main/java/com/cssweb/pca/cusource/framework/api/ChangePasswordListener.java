package cn.unicompay.wallet.client.framework.api;

public interface ChangePasswordListener extends NetworkListener {

	public void onSuccess();

	public void onFail(int errorCode, String msg);
}

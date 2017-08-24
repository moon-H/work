package cn.unicompay.wallet.client.framework.api;

public interface ResetPasswordListener extends NetworkListener {

	public void onSuccess();

	public void onFail(int errorCode, String msg);
}

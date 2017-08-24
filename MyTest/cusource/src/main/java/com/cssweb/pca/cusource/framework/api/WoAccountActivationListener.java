package cn.unicompay.wallet.client.framework.api;

public interface WoAccountActivationListener extends NetworkListener {

	public void onSuccess();

	public void onFail(int errorCode, String msg);

}

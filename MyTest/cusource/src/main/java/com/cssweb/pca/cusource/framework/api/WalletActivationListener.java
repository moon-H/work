package cn.unicompay.wallet.client.framework.api;

public interface WalletActivationListener extends NetworkListener {

	public void onSuccess();
	public void onFail(int code,String msg);
	
}

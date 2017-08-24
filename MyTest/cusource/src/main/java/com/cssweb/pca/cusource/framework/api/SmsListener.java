package cn.unicompay.wallet.client.framework.api;

public interface SmsListener extends NetworkListener {

	public void onSuccess();
	
	public void onFail(String msg);
	
}

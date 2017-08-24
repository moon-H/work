package cn.unicompay.wallet.client.framework.api;

public interface AuthListener extends NetworkListener {
	public void onAuthFail();
	
	public void onAuthSuccess();
}

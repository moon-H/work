package cn.unicompay.wallet.client.framework.api;

public interface WoStateListener {
	public void onWoNotRegistered();
	
	public void onWoPreActivated();
	
	public void onWoActivated();
	
	public void onTerminated();
}

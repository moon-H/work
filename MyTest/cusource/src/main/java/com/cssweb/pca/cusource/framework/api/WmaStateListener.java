package cn.unicompay.wallet.client.framework.api;

public interface WmaStateListener {
	public void onWmaState(boolean wmaState);

	public void onNoSE();

	public void onWmaLocked();

}

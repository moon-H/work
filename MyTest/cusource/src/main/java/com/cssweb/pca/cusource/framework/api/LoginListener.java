package cn.unicompay.wallet.client.framework.api;

public interface LoginListener extends NetworkListener {

	public void onFinished();

	/**
	 * Called when mobile login fail.
	 */
	public void onLoginFail(int errorcode, String erroMsg);

	/**
	 * Wallet Account Status, WO Account Status, WMA Status is needed to be
	 * initialize.
	 */
	// public void onActivationNeeded();
	public void onNoSE();

}

package cn.unicompay.wallet.client.framework.api;

public interface ActivationListener extends NetworkListener {

	public void onFinished();

	/**
	 * Called when getting state is failed.
	 */
	public void onGettingStateFailed(int errorCode, String errorMsg);

	/**
	 * Wallet Account State, WO Account State, WMA State is needed to be
	 * initialize.
	 */
	public void onActivationNeeded();

	/**
	 * 
	 * */
	public void onNoSe();

}

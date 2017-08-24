package cn.unicompay.wallet.client.framework.api;

public interface TncListener extends NetworkListener{
	/**
	 * Called when receiving terms and condition.
	 * @param tnc
	 */
	public void onSuccess(String tnc);
	/**
	 * Called when error
	 */
	public void onFail(String msg);
}

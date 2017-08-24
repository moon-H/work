package cn.unicompay.wallet.client.framework.api;

public interface ServiceUpdateListener extends NetworkListener {
	/**
	 * There is nothing to update.
	 */
	public void onNoUpdate();
	/**
	 * This is called when there is an update.
	 * @param isOptional true if it is optional
	 * @param downUrl the URL for downloading application.
	 */
	public void onUpdate(boolean isOptional,String downUrl);
}

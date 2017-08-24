package cn.unicompay.wallet.client.framework.api;

public interface WalletUpdateListener extends NetworkListener {
	/**
	 * Called when no update
	 */
	public void onNoUpdate();

	/**
	 * Called when optional update
	 */
	public void onOptionalUpdate(final String updateUrl,
			final String appversionDesc);

	/**
	 * Called when mandatory update.
	 */
	public void onMandatoryUpdate(final String updateUrl,
			final String appversionDesc);

	public void onFailed(int errorCode, String errorMsg);
}

package cn.unicompay.wallet.client.framework.api;

public interface CRSResultCallback {
	/**
	 * Called when success.
	 */
	public void onSuccess();
	/**
	 * Called when fails.
	 * @param info
	 */
	public void onFail(Object info);
//	/**
//	 * Called when need PIN verification
//	 */
//	public void onNeedPIN();
}

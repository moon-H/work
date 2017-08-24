package cn.unicompay.wallet.client.framework.api;

public interface PostSetUpListener extends NetworkListener{
	/**
	 * <pre>
	 * Called when user accept terms and condition.
	 * So user can request activation code PIN.
	 * </pre>
	 */
	public void onAcceptTnc();
	/**
	 * Called when error
	 */
	public void onError();
}

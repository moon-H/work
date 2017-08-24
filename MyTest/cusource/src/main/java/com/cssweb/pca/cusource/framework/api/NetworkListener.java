package cn.unicompay.wallet.client.framework.api;

public interface NetworkListener {
	/**
	 * <pre>
	 * Called when there is no available networks.
	 * In this case,wallet usually show error pop-up and terminates.
	 * </pre>
	 */
	public void onNoNetwork();
	
	/**
	 * <pre>
	 * Called when there is no response from server even though mobile has currently network connection.
	 * </pre>
	 */
	public void onNoResponse();
	
	/**
	 * <pre>
	 * Called when the session was gone after specific time passed.
	 * </pre>
	 */
	public void onSessionTimeOut();
}

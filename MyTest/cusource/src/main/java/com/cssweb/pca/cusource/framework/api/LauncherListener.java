package cn.unicompay.wallet.client.framework.api;

public interface LauncherListener extends NetworkListener {
	/**
	 * Called when locked
	 */
	public void onLocked();

	// /**
	// * Called when being reset
	// */
	// public void onReset();

	/**
	 * Called when having no connected SE.
	 */
	public void onNoSE();

	// /**
	// * <pre>
	// * Called when wallet must go through set up.
	// * After set up, the WalletManager.onUpdate() must be called before moving
	// to home.
	// * This method is called in main UI thread.
	// * </pre>
	// * @see WalletManager#loginAndSync()
	// */
	// public void onSetUp();

	// /**
	// * <pre>
	// * Called when wallet must go through update process.
	// * The {@link WalletManager#loginAndSync(UpdateListener)} must be called
	// before moving to home.
	// * Usually, after updating, the screen moves to home.
	// * This method is called in main UI thread.
	// * </pre>
	// * @see WalletManager#loginAndSync()
	// */
	// public void onLoginAndSync();

	/**
	 * Indicates current wallet's status is installing.
	 */
	// public void onInstalling();

	/**
	 * Indicates current wallet's status is removing.
	 */
	// public void onDeleting();

	// /**
	// * Indicates UICC swapped
	// * @param isNewUser true if new user, false if already registered user
	// */
	// public void onUICCSwap(boolean isNewUser);
	//
	// /**
	// * Indicates device changed
	// * @param isNewUser true if new user, false if already registered user
	// */
	// public void onDeviceChanged(boolean isNewUser);

	/**
	 * Called when checked status
	 */
	public void onCompleted();

	/**
	 * Called when get status fail
	 */
	public void onFailed(int errorCode, String errorMsg);

	public void onNeedActivate(String customerId, String customerInfoExistYN);

	public void onNoRegistration();
}

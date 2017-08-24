package cn.unicompay.wallet.client.framework.api;

public interface WalletStateListener {
	public void onWalletNotRegistered();
	
	public void onWalletPreActivated();
	
	public void onWalletActivated();
	
	// WMA 삭제 요청을 OTA Proxy 로 보낸다.
	// 삭제 알림 (Dialog) 후 종료.
	public void onWalletPreTerminated();
	
	public void onWalletTerminated();
}

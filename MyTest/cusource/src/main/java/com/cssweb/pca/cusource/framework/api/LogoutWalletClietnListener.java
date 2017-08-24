package cn.unicompay.wallet.client.framework.api;

public interface LogoutWalletClietnListener extends NetworkListener{

	
	public void onLogoutSucess();
	
	public void onLogoutFail();
}

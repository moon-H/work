package cn.unicompay.wallet.client.framework.api;

public interface ResetListener extends NetworkListener{
	public void onReset();
	
	public void onFail(String msg);
}

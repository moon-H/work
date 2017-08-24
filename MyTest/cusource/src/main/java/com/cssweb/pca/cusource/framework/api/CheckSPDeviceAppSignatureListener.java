package cn.unicompay.wallet.client.framework.api;

public interface CheckSPDeviceAppSignatureListener extends NetworkListener {
	public void onSuccess();

	public void onFail();
}

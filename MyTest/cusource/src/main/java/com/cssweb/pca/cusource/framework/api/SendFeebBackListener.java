package cn.unicompay.wallet.client.framework.api;

public interface SendFeebBackListener extends NetworkListener {
	public void onSuccess();

	public void onFail();
}

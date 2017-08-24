package cn.unicompay.wallet.client.framework.api;

public interface RecordCustomerSpOrderListener extends NetworkListener {
	public void recordSuccess();

	public void recordFailed();
}

package cn.unicompay.wallet.client.framework.api.http.model;

public class RequestCustomerIdbyMobileUIDRs extends ResultRs {
	public static final String ACTIVE_STATE = "21001";
	public static final String LOCKED_STATE = "21003";
	public static final String PREACTIVE = "21006";
	public static final String NO_REGISTRATION = "21000";

	private String customerId;
	private String walletStatus;
	private String customerInfoExistYN;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getWalletStatus() {
		return walletStatus;
	}

	public void setWalletStatus(String walletStatus) {
		this.walletStatus = walletStatus;
	}

	public String getCustomerInfoExistYN() {
		return customerInfoExistYN;
	}

	public void setCustomerInfoExistYN(String customerInfoExistYN) {
		this.customerInfoExistYN = customerInfoExistYN;
	}

}

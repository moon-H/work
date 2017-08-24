package cn.unicompay.wallet.client.framework.api.http.model;

public class LoginWalletClientRs extends ResultRs {

	private int woAccountState = -1;
	private int walletSubscriptionState = -1;

	private String HRV;
	private boolean isChangedCard;
	private boolean isWoAccountLoginSuccess;

	public String getHRV() {
		return HRV;
	}

	public void setHRV(String hRV) {
		HRV = hRV;
	}

	public boolean isChangedCard() {
		return isChangedCard;
	}

	public void setChangedCard(boolean isChangedCard) {
		this.isChangedCard = isChangedCard;
	}

	public int getWalletSubscriptionState() {
		return walletSubscriptionState;
	}

	public void setWalletSubscriptionStatus(int walletSubscriptionState) {
		this.walletSubscriptionState = walletSubscriptionState;
	}

	public int getWoAccountStatus() {
		return woAccountState;
	}

	public void setWoAccountStatus(int woAccountStatus) {
		this.woAccountState = woAccountStatus;
	}

	public boolean isWoAccountLoginSuccess() {
		return isWoAccountLoginSuccess;
	}

	public void setWoAccountLoginSuccess(boolean isWoAccountLoginSuccess) {
		this.isWoAccountLoginSuccess = isWoAccountLoginSuccess;
	}

}

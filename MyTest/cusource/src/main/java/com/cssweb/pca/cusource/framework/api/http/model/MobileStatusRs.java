package cn.unicompay.wallet.client.framework.api.http.model;

public class MobileStatusRs extends ResultRs {
	
	private boolean isChangedCard;
	
	private int woAccountState = -1;
	private int walletSubscriptionState = -1;
	
	
	public boolean isChangedCard() {
		return isChangedCard;
	}

	public void setChangedCard(boolean isChangedCard) {
		this.isChangedCard = isChangedCard;
	}

	
	public int getWalletSubscriptionState() {
		return walletSubscriptionState;
	}

	public void setWalletSubscriptionState(int walletSubscriptionState) {
		this.walletSubscriptionState = walletSubscriptionState;
	}

	public int getWoAccountState()
	{
		return woAccountState;
	}

	public void setWoAccountState(int woAccountState)
	{
		this.woAccountState = woAccountState;
	}

}

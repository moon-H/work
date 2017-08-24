package cn.unicompay.wallet.client.framework.api.http.model;

public class CheckChangeDeviceRs extends ResultRs{

	/**
	 * 
	 */
	public static final int STATE_NO_CHANGE = 0;
	/**
	 * 
	 */
	public static final int STATE_CHANGED = 1;
	/**
	 * 
	 */
	public static final int STATE_NEW_PHONE = 2;

	/**
	 * 
	 */
	private int changeState = -1;

	/**
	 * @return
	 */
	public int getChangeState() {
		return changeState;
	}

	/**
	 * @param changeState
	 */
	public void setChangeState(int changeState) {
		this.changeState = changeState;
	}
	
}

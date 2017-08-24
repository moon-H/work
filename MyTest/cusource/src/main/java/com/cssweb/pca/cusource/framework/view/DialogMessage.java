package cn.unicompay.wallet.client.framework.view;

import android.app.Activity;

public interface DialogMessage {
	
	public static int OK_BUTTON_INDEX = 0;
	
	/**
	 * @param index
	 * @param action
	 */
	public void setActionForBtn(int index,Runnable action);
	/**
	 * Returns the designated action for button at the index.
	 * @param index
	 * @return
	 */
	public Runnable getActionForBtn(int index);
	
	/**
	 * Show dialog box
	 */
	public void show();
	
	/**
	 * Returns the current activity
	 * @return
	 */
	public Activity getActivity();
}

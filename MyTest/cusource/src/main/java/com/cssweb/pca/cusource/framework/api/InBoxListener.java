package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.InBoxMessage;

public interface InBoxListener extends NetworkListener{
	/**
	 * 
	 */
	public void onError();
	/**
	 * @param list
	 */
	public void onList(Vector<InBoxMessage> list);
}

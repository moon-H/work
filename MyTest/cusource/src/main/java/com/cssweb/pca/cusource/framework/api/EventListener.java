package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Event;

public interface EventListener extends NetworkListener {
	public void onError(int errorCode,String errorMsg);

	 public void onList(Vector<Event> list);
}

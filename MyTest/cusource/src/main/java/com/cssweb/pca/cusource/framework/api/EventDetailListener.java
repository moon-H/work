package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Event;
import cn.unicompay.wallet.client.framework.model.EventStore;

public interface EventDetailListener extends NetworkListener {
	public void onList(Vector<EventStore> stores);

	public void onError(int errorCode, String errorMsg);

	public void onDetail(Event event);
}

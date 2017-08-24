package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Event;

public class GetAllEventListRs extends ResultRs {
	private Vector<Event> allEventList = null;

	public Vector<Event> getEventList() {
		return allEventList;
	}

	public void setEventList(Vector<Event> eventList) {
		this.allEventList = eventList;
	}
}

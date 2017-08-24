package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.EventStore;

public class GetEventDetailRs extends ResultRs {
	private String eventId;
	private String eventDetailImageUrl;
	private String eventDetail;
	private String regionName;
	private Vector<EventStore> storeList;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventDetailImageUrl() {
		return eventDetailImageUrl;
	}

	public void setEventDetailImageUrl(String eventDetailImageUrl) {
		this.eventDetailImageUrl = eventDetailImageUrl;
	}

	public String getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}

	public Vector<EventStore> getStoreList() {
		return storeList;
	}

	public void setStoreList(Vector<EventStore> storeList) {
		this.storeList = storeList;
	}

}

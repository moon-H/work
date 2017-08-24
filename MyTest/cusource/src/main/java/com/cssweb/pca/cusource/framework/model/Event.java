package cn.unicompay.wallet.client.framework.model;

public class Event {
	private String eventId;
	private String brandId;
	private String imageUrl;
	private String eventName;
	private String eventIntro;
	private String region;
	private String eventDetail;
	private String eventDetailImageUrl;
	private String eventStartDate;
	private String eventEndDate;
	private String keyWord;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}

	public String getEventDetailImageUrl() {
		return eventDetailImageUrl;
	}

	public void setEventDetailImageUrl(String eventDetailImageUrl) {
		this.eventDetailImageUrl = eventDetailImageUrl;
	}

	public String getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventIntro() {
		return eventIntro;
	}

	public void setEventIntro(String eventIntro) {
		this.eventIntro = eventIntro;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}

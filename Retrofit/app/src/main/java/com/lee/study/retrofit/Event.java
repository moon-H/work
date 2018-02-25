package com.lee.study.retrofit;

import java.io.Serializable;

public class Event implements Serializable {

    public static final String OPEN_TYPE_NO_RES = "0";
    public static final String OPEN_TYPE_LOCAL = "1";
    public static final String OPEN_TYPE_HTML = "2";


    private int eventId;
    private String eventName;
    private String eventImageUrl;
    private String eventUrl;
    private String regDate;
    private String openType;

    public static String getOpenTypeNoRes() {
        return OPEN_TYPE_NO_RES;
    }

    public static String getOpenTypeLocal() {
        return OPEN_TYPE_LOCAL;
    }

    public static String getOpenTypeHtml() {
        return OPEN_TYPE_HTML;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", eventImageUrl='" + eventImageUrl + '\'' +
                ", eventUrl='" + eventUrl + '\'' +
                ", regDate='" + regDate + '\'' +
                ", openType='" + openType + '\'' +
                '}';
    }
}

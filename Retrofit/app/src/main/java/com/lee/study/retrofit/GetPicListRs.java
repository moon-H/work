package com.lee.study.retrofit;

import java.util.List;

/**
 * Created by lenovo on 2016/12/13.
 */

public class GetPicListRs extends Response {
    private List<Event> eventList;

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "GetPicListRs{" + "eventList=" + eventList + '}';
    }
}

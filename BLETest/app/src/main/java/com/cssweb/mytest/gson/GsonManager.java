package com.cssweb.mytest.gson;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwx on 2018/6/14.
 */
public class GsonManager {
    private static final String TAG = "GsonManager";

    public static void generateGson() {
        EventInfo eventInfo = new EventInfo();
        Location location = new Location();
        location.setLatitude(39.957702);
        location.setLongitude(116.334625);
        eventInfo.setCityCode("4402");
        eventInfo.setLocationInfo(location);

        EventInfo eventInfo2 = new EventInfo();
        Location location2 = new Location();
        location2.setLatitude(36.062007);
        location2.setLongitude(120.384893);
        eventInfo2.setCityCode("2660");
        eventInfo2.setLocationInfo(location2);

        List<EventInfo> list = new ArrayList<>();
        list.add(eventInfo);
        list.add(eventInfo2);

        Data data = new Data();
        data.setDataList(list);

        Gson gson = new Gson();
        String json = gson.toJson(data);
        Log.d(TAG, "JSON = " + json);

    }

    private static class Data {
        List<EventInfo> dataList;

        public List<EventInfo> getDataList() {
            return dataList;
        }

        public void setDataList(List<EventInfo> dataList) {
            this.dataList = dataList;
        }

        @Override
        public String toString() {
            return "Data0{" + "dataList=" + dataList + '}';
        }
    }

    private static class EventInfo {
        String cityCode;
        Location locationInfo;

        public Location getLocationInfo() {
            return locationInfo;
        }

        public void setLocationInfo(Location locationInfo) {
            this.locationInfo = locationInfo;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }

    private static class Location {
        double latitude;
        double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "Location{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
        }
    }

}

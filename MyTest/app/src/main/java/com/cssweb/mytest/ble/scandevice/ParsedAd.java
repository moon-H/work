package com.cssweb.mytest.ble.scandevice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by liwx on 2017/11/2.
 */

public class ParsedAd {

    public byte flags;
    public List<UUID> uuids = new ArrayList<>();
    public String localName;
    public short manufacturer;
    public String advData;

    @Override
    public String toString() {
        return "ParsedAd{" + "flags=" + flags + ", uuids=" + uuids + ", localName='" + localName + '\'' + ", manufacturer=" + manufacturer + ", advData='" + advData + '\'' + '}';
    }
}

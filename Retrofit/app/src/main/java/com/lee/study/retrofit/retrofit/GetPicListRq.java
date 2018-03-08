package com.lee.study.retrofit.retrofit;

import com.lee.study.retrofit.base.Request;

/**
 * Created by lenovo on 2016/12/13.
 */

public class GetPicListRq extends Request {
    private String walletId;
    private int flag;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "GetPicListRq{" +
            "walletId='" + walletId + '\'' +
            ", flag=" + flag +
            '}';
    }
}

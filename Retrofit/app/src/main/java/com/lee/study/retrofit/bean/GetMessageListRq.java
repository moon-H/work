package com.lee.study.retrofit.bean;


import com.lee.study.retrofit.base.PageInfo;
import com.lee.study.retrofit.base.Request;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class GetMessageListRq extends Request {
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "GetMessageListRq{" +
                "pageInfo=" + pageInfo +
                '}';
    }
}

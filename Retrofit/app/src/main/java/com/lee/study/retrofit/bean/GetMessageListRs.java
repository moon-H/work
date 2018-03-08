package com.lee.study.retrofit.bean;


import com.lee.study.retrofit.base.PageInfo;
import com.lee.study.retrofit.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class GetMessageListRs extends Response {
    private PageInfo pageInfo;
    private List<Message> messageList;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "GetMessageListRs{" +
                "pageInfo=" + pageInfo +
                ", messageList=" + messageList +
                '}';
    }
}

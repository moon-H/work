package com.lee.study.retrofit.base;

/**
 * Created by liwx on 2018/2/26.
 */

public interface OnHttpCallBack<T> {
    void onSuccess(T t);

    void onFailed(Result result);
}

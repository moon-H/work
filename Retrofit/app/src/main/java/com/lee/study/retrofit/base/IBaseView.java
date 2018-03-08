package com.lee.study.retrofit.base;

/**
 * Created by liwx on 2018/2/25.
 */

public interface IBaseView {
    /**
     * 操作失败,通过code区分錯誤類型
     *
     * @param result
     */
    void onCommonFailed(Result result);

    void onShowProgress(String msg);

    void onDismissProgress();
}

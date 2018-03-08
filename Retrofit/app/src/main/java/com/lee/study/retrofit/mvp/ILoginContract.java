package com.lee.study.retrofit.mvp;

import com.lee.study.retrofit.base.BasePresenter;
import com.lee.study.retrofit.base.IBaseView;
import com.lee.study.retrofit.base.OnHttpCallBack;
import com.lee.study.retrofit.base.Result;

/**
 * Created by liwx on 2018/2/25.
 * 登录Contract
 */

public interface ILoginContract {
    /**
     * V 视图层
     */
    interface ILoginView extends IBaseView {
        void senAuthCodeSuccess();
    }

    /**
     * P 连接M层和V层
     */
    interface ILoginPresenter extends BasePresenter {
        /**
         * 获取验证码
         */
        void sendAuthCode(String phoneNumber);

        /**
         * 登录
         *
         * @param phoneNumber 手机号
         * @param code        验证码
         */
        void requestLogin(String phoneNumber, String code);

        void loginByToken();

        void getInboxList();
    }

    /**
     * M 逻辑具体实现
     */
    interface ILoginModel {
        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         */
        void getLoginCode(String phoneNumber, OnHttpCallBack<Result> callBack);

        void getEventList();

        void loginByToken(String phoneNumber, String token);

        void getInboxList();

    }
}

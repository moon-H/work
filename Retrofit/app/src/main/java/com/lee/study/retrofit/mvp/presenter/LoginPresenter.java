package com.lee.study.retrofit.mvp.presenter;

import android.content.Context;

import com.lee.study.retrofit.base.OnHttpCallBack;
import com.lee.study.retrofit.base.Result;
import com.lee.study.retrofit.mvp.ILoginContract;
import com.lee.study.retrofit.mvp.model.LoginModel;

/**
 * Created by liwx on 2018/2/27.
 */

public class LoginPresenter implements ILoginContract.ILoginPresenter {
    private static final String TAG = "LoginPresenter";
    private Context mContext;
    private ILoginContract.ILoginView mILoginView;
    private LoginModel mLoginModel;

    public LoginPresenter(Context context, ILoginContract.ILoginView loginView) {
        mContext = context;
        mILoginView = loginView;
        mLoginModel = new LoginModel(context);
    }

    @Override
    public void sendAuthCode(String phoneNumber) {
        mLoginModel.getLoginCode(phoneNumber, new OnHttpCallBack<Result>() {
            @Override
            public void onSuccess(Result result) {
                mILoginView.senAuthCodeSuccess();
            }

            @Override
            public void onFailed(Result result) {
                mILoginView.onCommonFailed(result);
            }
        });
    }

    @Override
    public void requestLogin(String phoneNumber, String code) {
        mLoginModel.getEventList();
    }

    @Override
    public void loginByToken() {
        mLoginModel.loginByToken("15502142822", "1111");
    }

    @Override
    public void getInboxList() {
        mLoginModel.getInboxList();
    }
}

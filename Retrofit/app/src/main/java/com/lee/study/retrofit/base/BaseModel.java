package com.lee.study.retrofit.base;

import android.content.Context;
import android.util.Log;

import com.lee.study.retrofit.base.utils.CssPermissionDeniedException;
import com.lee.study.retrofit.base.utils.DeviceInfo;
import com.lee.study.retrofit.base.utils.Utils;
import com.lee.study.retrofit.bean.RequestWalletLoginRq;
import com.lee.study.retrofit.bean.RequestWalletLoginRs;
import com.lee.study.retrofit.mvp.model.LoginGateway;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by liwx on 2018/3/2.
 */

public class BaseModel {
    private static final String TAG = "BaseModel";
    private Context mContext;
    public LoginGateway mLoginGateway;

    public BaseModel(Context context) {
        mContext = context;
        mLoginGateway = new LoginGateway(context);
    }

    public void getData() {
        Log.d(TAG, "getdata");
    }

    public Function mFunction = new Function<Observable<Throwable>, ObservableSource<?>>() {
        @Override
        public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                @Override
                public ObservableSource<?> apply(Throwable throwable) throws Exception {
                    Log.d(TAG, "retryWhen 啦~~~~");
                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401) {
                        Log.d(TAG, "401啦~~~~~~~~~~");
                        return autoLoginClient();
                    }
                    return Observable.error(throwable);
                }

            });
        }
    };

    public ObservableSource<?> autoLoginClient() {
        RequestWalletLoginRq param = new RequestWalletLoginRq();
        param.setMsisdn("18774888728");
        param.setLoginPassword(Utils.generatePassword("111111"));
        param.setModelName(DeviceInfo.getDeviceModelName());
        try {
            param.setImei(DeviceInfo.getIMEI(mContext));
            param.setMobileId("111111");
            param.setOsName("ANDROID");
            param.setSeId(DeviceInfo.getICCID(mContext));
            param.setImsi(DeviceInfo.getIMSI(mContext));
            param.setWalletId(BizApplication.WALLET_ID);
        } catch (CssPermissionDeniedException e) {
            e.printStackTrace();
        }
        return mLoginGateway.loginByPwd(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<RequestWalletLoginRs>() {
            @Override
            public void accept(RequestWalletLoginRs requestWalletLoginRs) throws Exception {
                Log.d(TAG, "自动登录成功啦~~~~");
            }
        });
    }

}

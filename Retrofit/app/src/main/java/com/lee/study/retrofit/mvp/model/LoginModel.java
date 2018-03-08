package com.lee.study.retrofit.mvp.model;

import android.content.Context;
import android.util.Log;

import com.lee.study.retrofit.base.BaseModel;
import com.lee.study.retrofit.base.BizApplication;
import com.lee.study.retrofit.base.OnHttpCallBack;
import com.lee.study.retrofit.base.PageInfo;
import com.lee.study.retrofit.base.Result;
import com.lee.study.retrofit.base.utils.CssPermissionDeniedException;
import com.lee.study.retrofit.base.utils.DeviceInfo;
import com.lee.study.retrofit.base.utils.Utils;
import com.lee.study.retrofit.bean.GetMessageListRq;
import com.lee.study.retrofit.bean.GetMessageListRs;
import com.lee.study.retrofit.bean.RequestWalletLoginByTokenRq;
import com.lee.study.retrofit.bean.RequestWalletLoginByTokenRs;
import com.lee.study.retrofit.bean.RequestWalletLoginRq;
import com.lee.study.retrofit.bean.RequestWalletLoginRs;
import com.lee.study.retrofit.mvp.ILoginContract;
import com.lee.study.retrofit.retrofit.GetPicListRs;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


/**
 * Created by liwx on 2018/2/26.
 */

public class LoginModel extends BaseModel implements ILoginContract.ILoginModel {
    private static final String TAG = "LoginModel";
    private Context mContext;

    public LoginModel(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void getLoginCode(String phoneNumber, OnHttpCallBack<Result> callBack) {
        loginTest();        //        String signOrg = "msisdn=" + phoneNumber +
        // "&walletId=" +
        // BizApplication
        // .WALLET_ID +
        //            "&key=" + BizApplication.KEY;
        //        String sign = Utils.encodeByMD5(signOrg);
        //        Observable<SendAuthCodeBySmsRs> request = null;
        //        try {
        //            request = LoginGateway.sendAuthCode(phoneNumber, DeviceInfo.getICCID
        // (mContext),
        //                BizApplication.WALLET_ID, sign);
        //        } catch (CssPermissionDeniedException e) {
        //            e.printStackTrace();
        //        }
        //        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        // .map(new Function<SendAuthCodeBySmsRs, SendAuthCodeBySmsRs>() {
        //            @Override
        //            public SendAuthCodeBySmsRs apply(SendAuthCodeBySmsRs sendAuthCodeBySmsRs)
        // throws
        //                Exception {
        //                Log.d(TAG, "map----------");
        //                return null;
        //            }
        //        }).subscribe(new Consumer<SendAuthCodeBySmsRs>() {
        //            @Override
        //            public void accept(SendAuthCodeBySmsRs o) throws Exception {
        //                Log.d(TAG, "accept1----------" + o.toString());
        //            }
        //        }, new Consumer<Throwable>() {
        //            @Override
        //            public void accept(Throwable throwable) throws Exception {
        //                Log.d(TAG, "accept2----------");
        //                throwable.printStackTrace();
        //            }
        //        });
    }

    @Override
    public void getEventList() {
        Observable<GetPicListRs> request = mLoginGateway.getEventList(0, "1%");
        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<GetPicListRs>() {
            @Override
            public void accept(GetPicListRs res) throws Exception {
                Log.d(TAG, "getEventList1----------" + res.toString());
                Log.d(TAG, "getEventList1----------" + res.getResult().getCode());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "accept2");
            }
        });
    }

    @Override
    public void loginByToken(String phoneNumber, String token) {
        RequestWalletLoginByTokenRq param = new RequestWalletLoginByTokenRq();
        param.setToken(token);
        param.setWalletId(BizApplication.WALLET_ID);
        param.setMsisdn(phoneNumber);
        param.setModelName(DeviceInfo.getDeviceModelName());
        param.setImei("imei");
        param.setSeId("seid");
        param.setImsi("imsi");
        param.setOsName("android");
        param.setVersionName("versionname");

        Observable<RequestWalletLoginByTokenRs> request = mLoginGateway.loginByToken(param);
        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestWalletLoginByTokenRs>() {
            @Override
            public void accept(RequestWalletLoginByTokenRs res) throws Exception {
                Log.d(TAG, "loginByToken----------" + res.toString());
                Log.d(TAG, "loginByToken----------" + res.getResult().getCode());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "accept2");
            }
        });

    }

    @Override
    public void getInboxList() {
        final GetMessageListRq param = new GetMessageListRq();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNumber(1);
        pageInfo.setPageSize(10);
        param.setPageInfo(pageInfo);

        final Observable<GetMessageListRs> request = mLoginGateway.getInboxList(param).retryWhen(mFunction);
        request
            //            .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            //                @Override
            //                public ObservableSource<?> apply(Observable<Throwable> observable) throws Exception {
            //                    return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            //                        @Override
            //                        public ObservableSource<?> apply(Throwable throwable) throws Exception {
            //                            //                        HttpException httpException = (HttpException)
            //                            // throwable;
            //                            Log.d(TAG, "retryWhen 啦~~~~");
            //                            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401) {
            //                                Log.d(TAG, "401啦~~~~~~~~~~");
            //                                return autoLoginClient();
            //                            }
            //                            return Observable.error(throwable);
            //                        }
            //
            //                    });
            //                }
            //            })
            //            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<GetMessageListRs>() {
                @Override
                public void accept(GetMessageListRs res) throws Exception {
                    Log.d(TAG, "getInboxList----------" + res.toString());
                    Log.d(TAG, "getInboxList----------" + res.getResult().getCode());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.d(TAG, "accept exception");
                    throwable.printStackTrace();
                    if (throwable instanceof HttpException) {
                        HttpException ex = (HttpException) throwable;
                        Log.e(TAG, "xxxxhttp code = " + ex.code());
                    }
                }
            });

    }


    public void loginTest() {
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
        mLoginGateway.loginByPwd(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestWalletLoginRs>() {
            @Override
            public void accept(RequestWalletLoginRs requestWalletLoginRs) throws Exception {
                Log.d(TAG, "登录成功啦 ~~~");
                temptest();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "登录异常啦 ~~~");
            }
        });
    }

    private void temptest() {
        final GetMessageListRq param2 = new GetMessageListRq();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNumber(1);
        pageInfo.setPageSize(10);
        param2.setPageInfo(pageInfo);
        mLoginGateway.getInboxList(param2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<GetMessageListRs>() {
            @Override
            public void accept(GetMessageListRs res) throws Exception {
                Log.d(TAG, "获取活动成功~~~" + res.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "获取活动异常啦~~~");
                throwable.printStackTrace();
            }
        });
    }

}

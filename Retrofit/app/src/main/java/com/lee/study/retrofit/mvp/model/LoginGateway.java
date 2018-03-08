package com.lee.study.retrofit.mvp.model;

import android.content.Context;
import android.util.Log;

import com.lee.study.retrofit.base.BaseGateway;
import com.lee.study.retrofit.base.BizApplication;
import com.lee.study.retrofit.base.utils.CssPermissionDeniedException;
import com.lee.study.retrofit.base.utils.DeviceInfo;
import com.lee.study.retrofit.base.utils.Utils;
import com.lee.study.retrofit.bean.GetMessageListRq;
import com.lee.study.retrofit.bean.GetMessageListRs;
import com.lee.study.retrofit.bean.RequestWalletLoginByTokenRq;
import com.lee.study.retrofit.bean.RequestWalletLoginByTokenRs;
import com.lee.study.retrofit.bean.RequestWalletLoginRq;
import com.lee.study.retrofit.bean.RequestWalletLoginRs;
import com.lee.study.retrofit.bean.SendAuthCodeBySmsRq;
import com.lee.study.retrofit.bean.SendAuthCodeBySmsRs;
import com.lee.study.retrofit.retrofit.GetPicListRq;
import com.lee.study.retrofit.retrofit.GetPicListRs;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by liwx on 2018/2/25.
 */

public class LoginGateway extends BaseGateway {
    private static final String TAG = "LoginGateway";
    private LoginService sService = sRetrofit.create(LoginService.class);
    private Context mContext;

    public LoginGateway(Context context) {
        super(context);
        mContext = context;
    }

    public Observable<RequestWalletLoginRs> loginByPwd(RequestWalletLoginRq req) {
        return sService.loginByPwd(getRequestBody(req)).compose(applySchedulers());
    }

    public Observable<GetPicListRs> getEventList(int flag, String walletId) {
        GetPicListRq req = new GetPicListRq();
        req.setFlag(flag);
        req.setWalletId(walletId);
        return sService.getEventList(getRequestBody(req));
    }

    public Observable<SendAuthCodeBySmsRs> sendAuthCode(String phoneNumber, String seId, String walletId, String sign) {
        SendAuthCodeBySmsRq req = new SendAuthCodeBySmsRq();
        req.setSign(sign);
        req.setWalletId(walletId);
        req.setSeId(seId);
        req.setMsisdn(phoneNumber);
        return sService.sendAuthCode(getRequestBody(req));
    }

    public Observable<RequestWalletLoginByTokenRs> loginByToken(RequestWalletLoginByTokenRq req) {
        return sService.loginByToken(getRequestBody(req));
    }

    public Observable<GetMessageListRs> getInboxList(GetMessageListRq req) {
        return sService.getInboxList(getRequestBody(req)).compose(applySchedulers());
    }

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
        return loginByPwd(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<RequestWalletLoginRs>
            () {
            @Override
            public void accept(RequestWalletLoginRs requestWalletLoginRs) throws Exception {
                Log.d(TAG, "自动登录成功啦~~~~");
            }
        });
    }

    public interface LoginService {
        @Headers({"Content-Type: application/json; charset=UTF-8"})
        @POST(BaseGateway.BASE_PATH + "event/getPicList")
        Observable<GetPicListRs> getEventList(@Body RequestBody body);

        @Headers({BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "activate/sendAuthCodeBySms")
        Observable<SendAuthCodeBySmsRs> sendAuthCode(@Body RequestBody body);

        @Headers({BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "client/requestWalletLoginByToken")
        Observable<RequestWalletLoginByTokenRs> loginByToken(@Body RequestBody body);

        @Headers({BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "inbox/getMessageList")
        Observable<GetMessageListRs> getInboxList(@Body RequestBody body);

        @Headers({BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "client/requestWalletLogin")
        Observable<RequestWalletLoginRs> loginByPwd(@Body RequestBody body);
    }
}

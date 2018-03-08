package com.lee.study.retrofit.base;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by liwx on 2018/2/25.
 */
public class BaseGateway {
    private static final String TAG = "BaseGateway";
    private static final String BASE_URL = "http://120.52.9.6:8980";
    public static final String BASE_PATH = "corpay/ci/";
    public static final String BASE_HEAD = "Content-Type: application/json; charset=UTF-8";
    private static Gson sGson = new Gson();
    public final CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());//cookie 持久化
    private HttpLoggingInterceptor logging;
    private OkHttpClient client;
    public Retrofit sRetrofit;

    public BaseGateway(Context context) {
        logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(logging).cookieJar(cookieJar)
            .connectTimeout(10, TimeUnit.SECONDS).
            readTimeout(60, TimeUnit.SECONDS).build();
        sRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory
            (CustomGsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory
            .create()).client(client) // 使用RxJava作为回调适配器
            .build();
        OkHttpUtils.initClient(client);
    }


    public RequestBody getRequestBody(Request request) {
        return RequestBody.create(MediaType.parse("Content-Type, " + "application/json"),
            convertRequest(request));
    }

    private String convertRequest(Request request) {
        try {
            JsonElement localJsonElement = sGson.toJsonTree(request);
            JsonObject requestJsonObject = new JsonObject();
            requestJsonObject.add(request.getClass().getSimpleName(), localJsonElement);
            Log.d(TAG, "sendRequest::request " + "::" + " " + requestJsonObject.toString());
            return new String(requestJsonObject.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public ObservableTransformer applySchedulers() {
        return mObservableTransformer;
    }


    private ObservableTransformer mObservableTransformer = new ObservableTransformer<Response,
        Object>() {
        @Override
        public ObservableSource<Object> apply(Observable<Response> observable) {
            return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread
                ()).flatMap(new Function<Response, ObservableSource<?>>() {
                @Override
                public ObservableSource<?> apply(final Response response) throws Exception {
                    Log.d(TAG, "in ObservableTransformer");
                    //                    return Observable.error(new NullPointerException("嘿嘿嘿"));
                    return Observable.create(new ObservableOnSubscribe<Response>() {
                        @Override
                        public void subscribe(ObservableEmitter<Response> e) throws Exception {
                            Log.d(TAG, "######### Observable.create");
                            e.onNext(response);
                        }
                    });
                }
            });
        }
    };
}

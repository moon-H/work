package com.lee.study.retrofit.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lee.study.retrofit.R;
import com.lee.study.retrofit.base.BaseGateway;
import com.lee.study.retrofit.base.Request;
import com.lee.study.retrofit.base.utils.Utils;
import com.lee.study.retrofit.bean.GetMessageListRs;
import com.lee.study.retrofit.bean.RequestWalletLoginRs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String API_URL = "http://120.52.9.6:8980";
    //    public static final String API_URL = "https://api.github.com";
    public static final String BASE_PATH = "corpay/ci/";
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGson = new Gson();
        Log.d(TAG, "### trace 1");
        test();
        Log.d(TAG, "### trace 2^");
        //        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //            }
        //        }).start();
    }

    private void test() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory
            (GsonConverterFactory.create()).client(client.build()).build();
        // Create an instance of our GitHub API interface.
        //        GitHub github = retrofit.create(GitHub.class);
        SKPEvent skpEvent = retrofit.create(SKPEvent.class);
        GetPicListRq req = new GetPicListRq();
        req.setFlag(0);
        req.setWalletId("000001");
        //        String param = "{'GetPicListRq':{'flag':0,'walletId':'000001'}}";
        String param = converRequest(req);
        Log.d(TAG, "param = " + param);
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, " +
            "application/json"), param);

        // Create a call instance for looking up Retrofit contributors.
        Call<Object> call = skpEvent.getEventList(requestBody);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, " main thread = " + Utils.isInMainThread());
                String res = response.body().toString();
                Log.d(TAG, "isSuccessful = " + response.code());
                Log.e(TAG, "onResponse=" + response.toString());
                Log.e(TAG, "biz res =" + res);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable ex) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                printWriter.close();
                Log.e(TAG, "onFailure:" + writer.toString());
            }
        });
        // Fetch and print a list of the contributors to the library.
    }

    //https://www.shankephone.com:8180/corpay/ci/event/getPicList
    public interface SKPEvent {
        //        @GET("/repos/{owner}/{repo}/contributors")
        //        Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo")
        // String repo);
        //        @POST("corpay/ci/event/getPicList")
        @Headers({"Content-Type: application/json; charset=UTF-8"})
        @POST(BASE_PATH + "event/getPicList")
        Call<Object> getEventList(@Body RequestBody body);


        @Headers({BaseGateway.BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "inbox/getMessageList")
        Observable<GetMessageListRs> getInboxList(@Body RequestBody body);

        @Headers({BaseGateway.BASE_HEAD})
        @POST(BaseGateway.BASE_PATH + "client/requestWalletLogin")
        Observable<RequestWalletLoginRs> loginByPwd(@Body RequestBody body);


    }

    private String converRequest(Request request) {
        try {
            JsonElement localJsonElement = this.mGson.toJsonTree(request);
            JsonObject requstJsonObject = new JsonObject();
            requstJsonObject.add(request.getClass().getSimpleName(), localJsonElement);
            Log.d(TAG, "sendRequest::request :: " + requstJsonObject.toString());
            return new String(requstJsonObject.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }
}

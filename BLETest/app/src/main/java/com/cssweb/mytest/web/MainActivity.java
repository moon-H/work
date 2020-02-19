package com.cssweb.mytest.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.cssweb.mytest.R;

public class MainActivity extends AppCompatActivity {
    //    private WebView mWebview;
    private ShankeWebView mWebview;

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebview = (ShankeWebView) findViewById(R.id.webview);
        // 启用javascript
        //        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.loadUrl("file:///android_asset/123.html");
        mWebview.addJavascriptInterface(this, "app");
    }

    @JavascriptInterface
    public void clickShareBtn() {
        Log.d("!23", "click0----------");
        Toast.makeText(getApplicationContext(), "不带参数----", Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public void clickBackHomeBtn(String msg) {
        Log.d("!23", "click111----------"+msg);
        Toast.makeText(getApplicationContext(), "不带参数----", Toast.LENGTH_SHORT).show();
    }
    //
//    @JavascriptInterface
//    public void click0(String data1, String data2) {
//        Toast.makeText(getApplicationContext(), "带参数---" + data1 + "---" + data2, Toast.LENGTH_SHORT).show();
//    }
}

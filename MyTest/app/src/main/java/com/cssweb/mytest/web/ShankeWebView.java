package com.cssweb.mytest.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by liwx on 2015/9/17.
 */
public class ShankeWebView extends WebView {

    private static final String TAG = "ShankeWebView";

    private Context context;
    private ProgressBar progressbar;

    public ShankeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setWebViewClient(new ShankeWebViewClient());
        setWebChromeClient(new WebChromeClient());
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        addView(progressbar);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("UTF-8");
        //        if (android.os.Build.VERSION.SDK_INT < 14) {
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        getSettings().setSupportZoom(true);
        // if(!AlopexApplication.sIsGalaxyS3) {
        // settings.setDefaultZoom(android.webkit.WebSettings.ZoomDensity.FAR);
        // }
        
    }

    //    public void setAndroidBridge(Object object) {
    //        this.addJavascriptInterface(object, OPTUS_ANDROID_BRIDGE);
    //    }

    //    public void setOnOptusPageFinishedListener(OnOptusPageFinishedListener listener) {
    //        this.listener = listener;
    //    }

    private class ShankeWebViewClient extends WebViewClient {

        public ShankeWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //            Toast.makeText(context, "start load url = " + url, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}

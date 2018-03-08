package com.lee.study.retrofit.base;

import android.support.v4.app.FragmentActivity;

/**
 * Created by liwx on 2018/3/7.
 */

public class BaseActivity extends FragmentActivity implements IBaseView {
    @Override
    public void onCommonFailed(Result result) {

    }

    @Override
    public void onShowProgress(String msg) {

    }

    @Override
    public void onDismissProgress() {

    }
}

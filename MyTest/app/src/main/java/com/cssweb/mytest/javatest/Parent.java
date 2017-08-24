package com.cssweb.mytest.javatest;

import android.util.Log;

/**
 * Created by lenovo on 2015/12/28.
 */
public abstract class Parent {
    public void test() {
        Log.d("123", getResponseCode() + "");
    }

    public Parent() {

    }

    protected abstract int getResponseCode();
}

package com.cssweb.mytest.javatest;

/**
 * Created by lenovo on 2015/12/28.
 */
public class Child extends Parent {
    public Child() {
        super();
    }

    @Override
    protected int getResponseCode() {
        return 13579;
    }
}

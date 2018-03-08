package com.lee.study.retrofit.base.utils;

/**
 * Created by liwx on 2018/2/27.
 */

public class CssPermissionDeniedException extends Exception {
    private String message;

    public String getMessage() {
        return message;
    }

    public CssPermissionDeniedException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CssPermissionDeniedException [message=" + message + "]";
    }
}

package com.lee.study.retrofit;

/**
 * Created by liwx on 2018/2/5.
 */

public class Result {
    public static final int OK = 0;
    private int code;
    private String message;

    public Result() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "Result [code=" + this.code + ", message=" + this.message + "]";
    }
}

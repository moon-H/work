package com.cssweb.mytest;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/4/7.
 */
public class CommonToast {
    public static void toast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}

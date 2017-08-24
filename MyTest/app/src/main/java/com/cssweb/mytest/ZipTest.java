package com.cssweb.mytest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.cssweb.mytest.javatest.Child;
import com.cssweb.mytest.javatest.Parent;

import java.io.File;

public class ZipTest extends FragmentActivity {
    private static final String TAG = ZipTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_test);

        File cachefile = getExternalCacheDir();
        final File extFile1 = getExternalFilesDir(null);
        final File extFile2 = getExternalFilesDir("12");
        if (cachefile != null) {
            String path = cachefile.getPath();
            Log.d(TAG, "cachefile = " + path);
        }
        Log.d(TAG, "path 1= " + extFile1.getPath());
        Log.d(TAG, "path 1.1= " + extFile1.getPath());
        Log.d(TAG, "path 2= " + extFile2.getPath());
        Log.d(TAG, "path 3= " + getCacheDir().getPath());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "unzip start------ ");
                try {
                    Utils.UnZipFolder(extFile1.getPath() + File.separator + "temp.zip", extFile2.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "error = " + e.getMessage());
                }
                Utils.deleteFile(extFile1.getPath() + File.separator + "temp.zip");
            }
        }).start();
        Parent parent = new Child();
        parent.test();
        //        int d = Log.d(TAG, "parent  = " + parent.test());
        Log.d("####", "####signature = " + Utils.getSignature(ZipTest.this, "com.cssweb.shankephone"));
        int color = Color.parseColor("#5fbad7");
        Button button = (Button) findViewById(R.id.btn_haha);
        button.setBackgroundColor(color);

    }

}
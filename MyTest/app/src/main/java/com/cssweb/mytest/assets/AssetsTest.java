package com.cssweb.mytest.assets;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lenovo on 2016/10/12.
 */
public class AssetsTest extends Activity {
    private static final String TAG = AssetsTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocalCityFromAsset();
    }

    //获取assets目录
    private String[] getLocalCityFromAsset() {
        AssetManager manager = getAssets();
        try {
            String[] list = manager.list("metro");
            //            for (int i = 0; i < list.length; i++) {
            //                Log.d(TAG, "file name  = " + list[i]);
            //                String verion = getAssetFileContent("metro" + File.separator + list[i] + File.separator + "version");
            //                Log.d(TAG, "VERSION = " + verion);
            //
            //            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    //
    private String getAssetFileContent(String path) {
        AssetManager assetManager = getAssets();
        String content = "";
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return content;
        }
        return content;
    }
}

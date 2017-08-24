package com.cssweb.mytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lenovo on 2016/7/8.
 */
public class NewTestActivity extends Activity {
    private static final String TAT = NewTestActivity.class.getSimpleName();
    private String cmd_install = "pm install -r ";//静默安装命令
    private String cmd_uninstall = "pm uninstall ";//静默卸载命令
    String apkLocation = Environment.getExternalStorageDirectory().toString() + "/";
    String packageName = "com.cssweb.shankephone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                onClick_install();
                final String cmd = "pm install -r /storage/sdcard0/ShankePhone_2.2.6_UAT_20160708_CM_debug.apk";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("123", "@@@@@@@@@@@@22");
                        try {
                            Runtime.getRuntime().exec(cmd);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("123", "###########3");

                    }
                }).start();
            }
        });

    }


    public void onClick_install() {
        String cmd = cmd_install + apkLocation + packageName;
        System.out.println("静默安装命令：" + cmd);
        excuteSuCMD(cmd);
    }

    public void onClick_uninstall(View view) {
        String cmd = cmd_uninstall + packageName;
        // String cmd = cmd_uninstall + "com.kingsoft.website";
        System.out.println("静默卸载命令：" + cmd);
        excuteSuCMD(cmd);
    }

    //执行shell命令
    protected int excuteSuCMD(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream((OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入library path可解决该问题
            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            int result = process.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

}

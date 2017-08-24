package com.cssweb.mytest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/6/29.
 */
public class ThreadTestActivity extends FragmentActivity {
    private static final String TAG = "ThreadTestActivity";
    private ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private ExecutorService mWatchExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        threadTest();
        //        forTest();
        testCommand();
    }

    private void forTest() {
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 5) {
                String s = null;
                try {
                    s.equals("1");
                } catch (Exception e) {
                    Log.d(TAG, "break = " + i);
                    break;
                }
            }

            Log.d(TAG, "### forTest = " + i);

        }
    }

    private void threadTest() {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            mSingleThreadExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        Log.d(TAG, "### thread = " + finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        mSingleThreadExecutor.shutdown();


        mWatchExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!mSingleThreadExecutor.isTerminated()) {
                    Log.d(TAG, "### waite");
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                Log.d(TAG, "### 完成");

            }
        });
        Log.d(TAG, "### thread complete");
    }

    private void testCommand() {
        String cmd = "chmod -R 777 /data/Afc";
        Runtime runtime = Runtime.getRuntime();
        Log.d(TAG, "execute shell [ " + cmd + " ]");
        try {
            Process process = runtime.exec(cmd);        //这句话就是shell与高级语言间的调用

            //使用exec执行不会等执行成功以后才返回,它会立即返回
            //所以在某些情况下是很要命的(比如复制文件的时候)
            //使用waitFor()可以等待命令执行完成以后才返回
            if (process.waitFor() != 0) {
                Log.d(TAG, "execute shell exitValue = [ " + process.exitValue() + " ]");
            } else {
                Log.d(TAG, "execute shell exitValue = 0");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "execute shell occur an InterruptedException :: ", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "execute shell occur an IOException :: ", e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "execute shell occur an Exception :: ", e);
        }
    }

    private void imsiTetst() {
    }
}

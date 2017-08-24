package com.cssweb.mytest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cssweb.mytest.arc.ArcProgress;
import com.cssweb.mytest.javatest.Child;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static android.R.attr.bottomLeftRadius;
import static android.R.attr.bottomRightRadius;
import static android.R.attr.topLeftRadius;
import static android.R.attr.topRightRadius;

/**
 * Created by lenovo on 2016/2/18.
 */
public class TestActivity extends Activity implements View.OnClickListener {
    float mTopLeftRadius=10;
    float mTopRightRadius=5;
    float mBottomRightRadius=0;
    float mBottomLeftRadius=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        int strokeWidth = 5; // 3dp 边框宽度
        int roundRadius = 15; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#2E3135");//边框颜色
        int fillColor = Color.parseColor("#EE8101");//内部填充颜色
float[] ra=new  float[]{5,0};
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadii(new float[] { mTopLeftRadius,
            mTopLeftRadius, mTopRightRadius, mTopRightRadius,
            mBottomRightRadius, mBottomRightRadius, mBottomLeftRadius,
            mBottomLeftRadius });//        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);


        handleArcProgress();

        Button button= (Button) findViewById(R.id.btn_start);
        button.setBackground(gd);


        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_start2).setOnClickListener(this);
        findViewById(R.id.btn_stop2).setOnClickListener(this);
        Child parent = new Child();
        parent.test();
        byte[] b = new byte[]{(byte) 0xAA};
        //        Integer parseAmount = Integer.parseInt(HexConverter.bytesToHexString(b), 10);

        //        Log.d("123", "value = " +parseAmount );
        byte[] bb = new byte[0];
        try {
            bb = HexConverter.hexStringToBytes("42");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < bb.length; i++) {
            Log.d("123", " value = " + bb[i] + "\n");
        }
        SimpleDateFormat format4 = new SimpleDateFormat("MMddHHmmss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");

        Log.d("123", getDeviceInfo(this));
        Date date1 = null;
        try {
            date1 = format4.parse("0426130817");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long currentVal = date1.getTime();
        Log.d("123", "### = " + format2.format(currentVal));
        //        Log.d("123", "################");
        //
        //        testThreadPool();

        singleThreadExcutorTest();

        short sjt = 0x0100;
        //        Log.d("123", "###3 = " + Integer.parseInt("0100", 16));
        //        Log.d("123", "###3 = " + (256 == sjt));
        //        Log.d("123", "###staus bar = " + DeviceInfo.getStatusBarHeight(this));
        //        Log.d("123", "###density = " + DeviceInfo.getDensity(this));
        //        Log.d("123", "###w = " + DeviceInfo.getScreenWidth(this) + " h = " + DeviceInfo.getScreenHeight(this));
        //        Log.d("123", "### 2<<<3 = " + (2 << 3));

        Field[] fields = Build.class.getDeclaredFields();
        //        for (Field field : fields) {
        //            try {
        //                field.setAccessible(true);
        //                //                mInfosMap.put(field.getName(), field.get(null).toString());
        //                Log.d("132", field.getName() + " : " + field.get(null));
        //            } catch (Exception e) {
        //                Log.d("123", "an error occur when collect crash info.." + e.getMessage(), e);
        //            }
        //        }
        //        Log.d("123", "while test = " + testWhile());

    }

    private void testThreadPool() {
        Log.d("123", "################testThreadPool start");

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("123", "######  hahaha1");
            }
        });
        Log.d("123", "################11111111111111111");

        mExecutor.shutdown();
        Log.d("123", "################22222222222222");
        try {
            mExecutor.awaitTermination(5, TimeUnit.SECONDS);
            while (!mExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                Log.d("123", "###############qqqqqqqqqqqqq");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("123", "################testThreadPool end");
    }

    private void singleThreadExcutorTest() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        Log.d("123", "singleThreadExcutorTest " + index);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    Timer timer = new Timer();
    ArcProgress arcProgress2;
    ArcProgress arcProgress2_1;

    private void handleArcProgress() {
        final ArcProgress arcProgress1 = (ArcProgress) findViewById(R.id.balance_progress1);
        arcProgress2 = (ArcProgress) findViewById(R.id.balance_progress2);
        //        arcProgress2_1 = (ArcProgress) findViewById(R.id.balance_progress2_1);
        ArcProgress arcProgress3 = (ArcProgress) findViewById(R.id.balance_progress3);

        //
        //        arcProgress2.setUnfinishedPaintType(Paint.Cap.BUTT);
        //        arcProgress2.setfinishedPaintType(Paint.Cap.BUTT);
        //
        //        arcProgress2_1.setfinishedPaintType(Paint.Cap.ROUND);
        //        arcProgress2_1.setUnfinishedPaintType(Paint.Cap.ROUND);

        //        arcProgress1.setDefaultAngle(0.63f);
        //        arcProgress2.setDefaultAngle(0.6f);
        //        //        arcProgress2_1.setDefaultAngle(0.68f);
        //        arcProgress3.setDefaultAngle(0.78f);
        int iaaa = (int) 1.8;
        Log.d("123", "$$$$$ i = " + iaaa);
        arcProgress1.setFinishedStrokeColor(getResources().getColor(R.color.finished_colors));
        arcProgress2.setFinishedStrokeColor(getResources().getColor(R.color.finished_colors));
        arcProgress3.setFinishedStrokeColor(getResources().getColor(R.color.finished_colors));
        arcProgress1.setUnfinishedStrokeColor(getResources().getColor(R.color.un_finished_colors));
        arcProgress2.setUnfinishedStrokeColor(getResources().getColor(R.color.un_finished_colors));
        arcProgress3.setUnfinishedStrokeColor(getResources().getColor(R.color.un_finished_colors));
        arcProgress2.setAntiClockWise(true);


        //        arcProgress2.setProgress(arcProgress2.getProgress() + 20);

        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                arcProgress2.setProgress(arcProgress2.getProgress() + 1);
        //
        //            }
        //        }, 2000);
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                arcProgress2.setProgress(arcProgress2.getProgress() + 1);
        //
        //            }
        //        }, 3000);

        //        CountDownTimer countDownTimer = new CountDownTimer(2000, 500) {
        //            @Override
        //            public void onTick(long millisUntilFinished) {
        //                if (!(arcProgress2.getProgress() >= arcProgress2.getMax()))
        //                    arcProgress2.setProgress(arcProgress2.getProgress() + 1);
        //            }
        //
        //            @Override
        //            public void onFinish() {
        //
        //            }
        //        };
        //
        //        countDownTimer.start();
        //                                            arcProgress2.setProgress(1000);
        arcProgress4 = (ArcProgress) findViewById(R.id.balance_progress4);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!(arcProgress2.getProgress() >= 20000)) {
                            Log.d("123", "记载中 " + i);
                            arcProgress2.setProgress(arcProgress2.getProgress() + 500);
                            arcProgress4.setProgress(arcProgress4.getProgress() + 500);
                        } else {
                            timer.cancel();
                            arcProgress2.isProgressStop(true);
                        }
                        Log.d("123", "i = " + i);
                        i++;
                    }

                });
            }
        }, 500, 50);
        Log.d("123", "##################################### = " + (1 % 6));
    }

    public int testWhile() {
        int i = 0;
        while (true) {
            Log.d("213", "1111 =" + i);
            if (i == 3) {
                return -1;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    ArcProgress arcProgress4;
    int i = 0;
    boolean flag = true;
    private ExecutorService mExecutor = Executors.newCachedThreadPool();


    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];

        // 使用4个byte表示int
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;  // 偏移量
            b[i] = (byte) ((value >> offset) & 0xFF); //每次取8bit
        }
        return b;
    }

    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static int bytesToInt(byte[] b) {
        String s = new String(b);
        return Integer.parseInt(s);
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            int shift = (b.length - 1 - i) * 8;
            value += (b[i + offset] & 0xFF) << shift;
        }
        return value;
    }

    @SuppressLint("NewApi")

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;

//        if (Build.VERSION.SDK_INT >= 23) {
//            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        } else {
//            PackageManager pm = context.getPackageManager();
//
//            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        }

        return result;
    }


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = null;

            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();

            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }


            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                start();
                break;
            case R.id.btn_stop:
                stop();
            case R.id.btn_start2:
                start2();
                break;
            case R.id.btn_stop2:
                stop2();
                break;
        }

    }

    Timer startTimer = new Timer();
    MyTask task;
    MyTask2 task2;

    private void start() {
        Log.d("123", "### start");
        task = new MyTask();
        startTimer.schedule(task, 0, 1000);
    }

    private void start2() {
        Log.d("123", "### start2");
        task2 = new MyTask2();
        startTimer.schedule(task2, 0, 1000);
    }

    private void stop() {
        Log.d("123", "### stop");
        task.cancel();
        //        startTimer.cancel();
    }

    private void stop2() {
        Log.d("123", "### stop2");
        task2.cancel();
        //        startTimer.cancel();
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            Log.d("123", "### sendHeartBeat");

        }
    }

    private class MyTask2 extends TimerTask {

        @Override
        public void run() {
            Log.d("123", "### check ticket");

        }
    }
}

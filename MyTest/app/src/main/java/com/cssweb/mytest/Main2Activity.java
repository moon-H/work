package com.cssweb.mytest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/***
 * Android垂直动画->用animation实现。 原理：每隔2秒，当前TextView做移除效果，下一个TextView做进入效果。
 * 注意：装载这个轮换TextView的LinearLayout，一定要设置一个只能显示一个TextView的高度，
 * 既->LinearLayout.height = TextView.height
 *
 * @author parcool
 */
public class Main2Activity extends Activity {
    private Animation anim_in, anim_out;
    private LinearLayout llContainer;
    private Handler mHandler;
    private boolean runFlag = true;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 如果是Home键等其他操作导致销毁重建，那么判断是否存在，
        // 如果存在，那么获取上次滚动到状态的index
        // 如果不存在，那么调用初始化方法
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt("currIndex");
            Log.d("tag", "The savedInstanceState.getInt value is" + index);
        } else {
            init();
        }
    }

    @SuppressLint("HandlerLeak")
    private void init() {
        // TODO Auto-generated method stub
        // 找到装载这个滚动TextView的LinearLayout
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        // 加载进入动画
        anim_in = AnimationUtils.loadAnimation(this, R.anim.anim_notice_in);
        // 加载移除动画
        anim_out = AnimationUtils.loadAnimation(this, R.anim.anim_notice_out);
        // 填充装文字的list
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add("滚动的文字" + i);
        }
        // 根据list的大小，动态创建同样个数的TextView
        for (int i = 0; i < list.size(); i++) {
            TextView tvTemp = new TextView(this);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            tvTemp.setGravity(Gravity.CENTER);
            tvTemp.setText(list.get(i));
            tvTemp.setId(i + 10000);
            llContainer.addView(tvTemp);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        // 移除
                        TextView tvTemp = (TextView) msg.obj;
                        Log.d("tag", "out->" + tvTemp.getId());
                        tvTemp.startAnimation(anim_out);
                        tvTemp.setVisibility(View.GONE);
                        break;
                    case 1:
                        // 进入
                        TextView tvTemp2 = (TextView) msg.obj;
                        Log.d("tag", "in->" + tvTemp2.getId());
                        tvTemp2.startAnimation(anim_in);
                        tvTemp2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    /***
     * 停止动画
     */
    private void stopEffect() {
        runFlag = false;
    }

    /***
     * 启动动画
     */
    private void startEffect() {
        runFlag = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (runFlag) {
                    try {
                        // 每隔2秒轮换一次
                        Thread.sleep(2000);
                        // 至于这里还有一个if(runFlag)判断是为什么？大家自己试验下就知道了
                        if (runFlag) {
                            // 获取第index个TextView开始移除动画
                            TextView tvTemp = (TextView) llContainer.getChildAt(index);
                            mHandler.obtainMessage(0, tvTemp).sendToTarget();
                            if (index < llContainer.getChildCount()) {
                                index++;
                                if (index == llContainer.getChildCount()) {
                                    index = 0;
                                }
                                // index+1个动画开始进入动画
                                tvTemp = (TextView) llContainer.getChildAt(index);
                                mHandler.obtainMessage(1, tvTemp).sendToTarget();
                            }
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        // 如果有异常，那么停止轮换。当然这种情况很难发生
                        runFlag = false;
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /***
     * 当页面暂停，那么停止轮换
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        stopEffect();
    }

    /***
     * 当页面可见，开始轮换
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        startEffect();
    }

    /***
     * 用于保存当前index的,结合onCreate方法
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt("currIndex", index);
    }

}

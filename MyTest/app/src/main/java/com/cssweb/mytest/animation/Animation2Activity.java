package com.cssweb.mytest.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cssweb.mytest.R;

/**
 * Created by lenovo on 2017/3/29.
 */
public class Animation2Activity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "Animation2Activity";
//    View left;
//    View right;
    Button mButton;

    View leftParent;
    View rightParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation2);
//        left = findViewById(R.id.lly_left);
//        right = findViewById(R.id.lly_right);
        leftParent = findViewById(R.id.lly_left_parent);
        rightParent = findViewById(R.id.lly_right_parent);


        rightParent.setOnClickListener(this);
        leftParent.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.btn_switch);
        mButton.setOnClickListener(this);
//        left.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, " LEFT  = " + right.getLeft());
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_parent:
                Toast.makeText(getApplicationContext(), "左侧", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lly_right_parent:
                Toast.makeText(getApplicationContext(), "右侧", Toast.LENGTH_SHORT).show();
//                int startX = right.getLeft();
//                Log.d(TAG, "startX = " + startX);


                break;
            case R.id.btn_switch:


                Log.d(TAG, "btn width = " + mButton.getWidth() + " left = " + leftParent.getWidth() + " right = " + leftParent.getWidth());
//                Log.d(TAG, "getleft before = " + right.getLeft());

                moveLeft();
                moveRight();
                //                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                //                    @Override
                //                    public void onAnimationUpdate(ValueAnimator animation) {
                //                    }
                //                });
                break;

        }
    }

    private void moveLeft() {
        ObjectAnimator rightAni = ObjectAnimator.ofFloat(leftParent, "translationX", 0, (mButton.getWidth() + leftParent.getWidth()));
        rightAni.setInterpolator(null);
        rightAni.setDuration(3000);
        rightAni.start();
        rightAni.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];
                leftParent.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                Log.d("test", "Screenx--->" + x + "  " + "Screeny--->" + y);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void moveRight() {
        ObjectAnimator rightAni = ObjectAnimator.ofFloat(rightParent, "translationX", 0, -(mButton.getWidth() + rightParent.getWidth()));
        rightAni.setInterpolator(null);
        rightAni.setDuration(3000);
        rightAni.start();

        rightAni.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];
                rightParent.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                Log.d("test", "Screenx--->" + x + "  " + "Screeny--->" + y);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

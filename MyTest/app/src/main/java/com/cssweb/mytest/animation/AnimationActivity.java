package com.cssweb.mytest.animation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cssweb.mytest.R;
import com.cssweb.mytest.utils.Utils;

/**
 * Created by liwx on 2016/4/4.
 */
public class AnimationActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "AnimationActivity";

    private VDHLayout parenLayout;
    private int comonMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        parenLayout = (VDHLayout) findViewById(R.id.content);
        //
        View dragView = findViewById(R.id.lly_drag);
        //        addChildView();
        parenLayout.setDragView(dragView);

    }

    private void addChildView() {
        Resources res = getResources();

        FrameLayout.LayoutParams lp0 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parenLayout.addView(getCardImageView("1", res.getDrawable(R.drawable.bj3)), lp0);

        comonMargin = Utils.px2dip(this, 200);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = comonMargin * 2;
        parenLayout.addView(getCardImageView("2", res.getDrawable(R.drawable.xy3)), lp);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.topMargin = comonMargin;
        parenLayout.addView(getCardImageView("3", res.getDrawable(R.drawable.yct3)), 1, lp2);

        parenLayout.setDragView(parenLayout.getChildAt(2));

    }

    private ImageView getCardImageView(String tag, Drawable drawable) {
        ImageView view = new ImageView(this);
        view.setTag(tag);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().trim();
        Log.d(TAG, "### onClick tag = " + tag);
        //        if (!TextUtils.isEmpty(tag)) {
        //            if (tag.equals("2")) {
        //                int count = parenLayout.getChildCount();
        //                Log.d(TAG, "start left = " + v.getLeft() + " top = " + v.getTop() + " right = " + v.getRight() + " bottom = " + v.getBottom());
        //                translateAnimationShow(v, 0, 100);
        //            }
        //        }
        //        Log.d(TAG, "height = " + v.getHeight());
    }

    private void translateAnimationShow(final View view, float y, float dtY) {
        Log.d(TAG, "start animation y = " + y + " dtY = " + dtY);
        TranslateAnimation animation = new TranslateAnimation(0, 0, y, dtY);
        animation.setDuration(5000);
        animation.setFillAfter(true);
        //        view.setAnimation(animation);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "end left = " + view.getX() + " top = " + view.getY() + " right = " + view.getRight() + " bottom = " + view.getBottom());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        String tag = v.getTag().toString().trim();
        Log.d(TAG, "onLongClick tag  = " + tag);
        parenLayout.setDragView(v);
        return true;
    }
}

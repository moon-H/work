package com.cssweb.mytest.animation;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cssweb.mytest.utils.DeviceInfo;

/**
 * Created by zhy on 15/6/3.
 */
public class VDHLayout extends RelativeLayout {
    private static final String TAG = "VDHLayout";
    private ViewDragHelper mDragger;

    private View mDragView;
    //    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mOriginPos = new Point();
    private Point mCurrentPos = new Point();
    private Point mMaxPos = new Point();
    private Context mContext;

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                Log.d(TAG, "tryCaptureView = " + (child == mDragView));
                return child == mDragView;

            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.d(TAG, "clampViewPositionHorizontal");
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView.getWidth() - leftBound;

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.d(TAG, "clampViewPositionVertical top = " + top + " dy = " + dy);
                if (dy > 0 && mCurrentPos.y == mOriginPos.y) {
                    return mOriginPos.y;
                } else
                    return top;
            }


            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                Log.d(TAG, "onViewReleased y= " + yvel);
                if (releasedChild == mDragView && mCurrentPos.y > mOriginPos.y) {
                    mDragger.settleCapturedViewAt(mOriginPos.x, mOriginPos.y);
                    invalidate();
                } else if (releasedChild == mDragView && mCurrentPos.y < mOriginPos.y) {
                    mDragger.settleCapturedViewAt(mMaxPos.x, mMaxPos.y);
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.d(TAG, "onEdgeDragStarted");
                //                mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.d(TAG, "getViewHorizontalDragRange");
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                Log.d(TAG, "getViewVerticalDragRange =" + (getMeasuredHeight() - child.getMeasuredHeight()));
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d(TAG, "onViewPositionChanged left = " + left + " top = " + top + " dx = " + dx + " dy= " + dy);
                mCurrentPos.y = top;
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //        Log.d(TAG, "onInterceptTouchEvent");
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //        Log.d(TAG, "onTouchEvent");
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        //        Log.d(TAG, "computeScroll");
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout");
        //        if (mAutoBackView != null) {
        mOriginPos.x = mDragView.getLeft();
        mOriginPos.y = mDragView.getTop();

        mCurrentPos.x = mDragView.getLeft();
        mCurrentPos.y = mDragView.getTop();

        mMaxPos.x = mDragView.getLeft();
        mMaxPos.y = DeviceInfo.getScreenHeight(mContext)  - mDragView.getHeight()-DeviceInfo.getStatusBarHeight(mContext);
        Log.d(TAG, "onLayout org y =" + mOriginPos.y);
        Log.d(TAG, "statusbar =" + DeviceInfo.getStatusBarHeight(mContext));
        Log.d(TAG, "ScreenHeight =" + DeviceInfo.getScreenHeight(mContext) + " NavigationBarHeight = " + DeviceInfo.getNavigationBarHeight(mContext) + " dragViewHeight" + mDragView.getHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate");
        //        mDragView = getChildAt(0);
        //        mAutoBackView = getChildAt(1);
        //        mEdgeTrackerView = getChildAt(2);
    }

    public void setDragView(View view) {
        Log.d(TAG, "setDragView");
        mDragView = view;
        //        mAutoBackView = view;
        //        //        invalidate();
        //        mOriginPos.x = mAutoBackView.getLeft();
        //        mOriginPos.y = mAutoBackView.getTop();
    }
}

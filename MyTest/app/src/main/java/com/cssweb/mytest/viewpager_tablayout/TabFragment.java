package com.cssweb.mytest.viewpager_tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cssweb.framework.utils.MLog;
import com.cssweb.mytest.R;

/**
 * Created by lenovo on 2017/7/20.
 */

public class TabFragment extends Fragment {
    private static final String TAG = "TabFragment";
    String mTitle;
    private View mRootView;

    public static TabFragment getInstance(String title) {
        TabFragment f = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if ((savedInstanceState = getArguments()) != null) {
            mTitle = savedInstanceState.getString("title");
            MLog.e(TAG, "title = " + mTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "onCreateView");
        if (mRootView == null) {
            MLog.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_vp, container, false);
            LinearLayout bg = (LinearLayout) mRootView.findViewById(R.id.lly_content);
            if (mTitle.equals("全部")) {
                bg.setBackgroundColor(getResources().getColor(R.color.aaa));
            } else if (mTitle.equals("未支付")) {
                bg.setBackgroundColor(getResources().getColor(R.color.un_finished_colors));
            } else if (mTitle.equals("未完成")) {
                bg.setBackgroundColor(getResources().getColor(R.color.st_total_price_text));
            } else if (mTitle.equals("已完成")) {
                bg.setBackgroundColor(getResources().getColor(R.color.about_tel));

            }
        }
        return mRootView;
    }
}
